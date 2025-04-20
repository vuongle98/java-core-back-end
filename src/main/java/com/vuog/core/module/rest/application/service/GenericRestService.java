package com.vuog.core.module.rest.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.common.exception.DataNotFoundException;
import com.vuog.core.common.util.ObjectMappingUtil;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import com.vuog.core.module.rest.domain.specification.SpecificationBuilder;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionHandler;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionRegistry;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenericRestService {

    private static final Logger logger = LoggerFactory.getLogger(GenericRestService.class);
    private final ProjectionHandler projectionHandler;
    private final ProjectionRegistry projectionRegistry;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;

    public GenericRestService(
            ProjectionHandler projectionHandler,
            ProjectionRegistry projectionRegistry,
            ObjectMapper objectMapper,
            ApplicationContext applicationContext) {
        this.projectionHandler = projectionHandler;
        this.projectionRegistry = projectionRegistry;
        this.objectMapper = objectMapper;
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <T, ID> GenericRepository<T, ID> getJpaRepository(String entity) {
        String repositoryBeanName = entity + "RepositoryImpl";
        return applicationContext.getBean(repositoryBeanName, GenericRepository.class);
    }

    public <T, ID> Class<T> getEntityClass(GenericRepository<T, ID> repository) {
        return repository.getEntityClass();
    }

    public <T, ID> Page<T> findAll(GenericRepository<T, ID> repository, Pageable pageable) {
        return repository.findAll(pageable);
    }

    public <T, ID, D> Page<D> findAll(GenericRepository<T, ID> repository, Pageable pageable, Class<D> projectionClass) {
        Page<T> page = repository.findAll(pageable);
        return page.map(entity -> projectWithNested(entity, projectionClass));
    }

    public <T, ID> T getById(GenericRepository<T, ID> repository, ID id) {
        return findById(repository, id).orElseThrow(() -> new DataNotFoundException("Not found entity with id: " + id));
    }

    public <T, ID, D> D getById(GenericRepository<T, ID> repository, ID id, Class<D> projectionClass) {
        T entity = findById(repository, id).orElseThrow(() -> new DataNotFoundException("Not found entity with id: " + id));
        return projectWithNested(entity, projectionClass);
    }

    public <T, ID> Optional<T> findById(GenericRepository<T, ID> repository, ID id) {
        return repository.findById(id);
    }

    public <T, ID> T save(GenericRepository<T, ID> repository, T entity) {
        return repository.save(entity);
    }

    public <T, ID> void delete(GenericRepository<T, ID> repository, ID id) {
        repository.deleteById(id);
    }

    public <T> List<Map<String, Object>> getProjectedData(List<T> data, List<String> fields) {
        return data.stream()
                .map(entity -> projectionHandler.project(entity, fields))
                .collect(Collectors.toList());
    }

    public <T> Page<T> findAll(
            JpaSpecificationExecutor<T> repository,
            Map<String, String> filters,
            Pageable pageable,
            Class<T> entityClass
    ) {
        Specification<T> spec = SpecificationBuilder.build(filters, entityClass);
        return repository.findAll(spec, pageable);
    }

    public <T, D> Page<D> findAll(
            JpaSpecificationExecutor<T> repository,
            Map<String, String> filters,
            Pageable pageable,
            Class<T> entityClass,
            Class<D> projectionClass
    ) {

        Specification<T> spec = SpecificationBuilder.build(filters, entityClass);
        Page<T> page = repository.findAll(spec, pageable);
        return page.map(entity -> projectWithNested(entity, projectionClass));
    }

    public <T, D> D project(T entity, Class<D> projectionClass) {
        return projectWithNested(entity, projectionClass);
    }

    @SuppressWarnings("unchecked")
    public <T, ID, D> D create(GenericRepository<T, ID> repository, Map<String, Object> createReq, Class<D> projectionClass) throws Exception {
        Class<T> entityClass = repository.getEntityClass();
        T entity = ObjectMappingUtil.map(createReq, entityClass);
        handleRelationships(entity, createReq, entityClass);
        T savedEntity = save(repository, entity);

        if (projectionClass != null) {
            try {
                Field idField = findIdField(entityClass);
                if (idField != null) {
                    idField.setAccessible(true);
                    ID entityId = (ID) idField.get(savedEntity);
                    return getById(repository, entityId, projectionClass);
                }
            } catch (Exception e) {
                // If the projection fails, return the full entity
                return (D) savedEntity;
            }
        }
        return (D) savedEntity;
    }

    @SuppressWarnings("unchecked")
    public <T, ID, D> D update(GenericRepository<T, ID> repository, ID id, Map<String, Object> updateReq, Class<D> projectionClass) throws Exception {
        T existingEntity = getById(repository, id);
        objectMapper.updateValue(existingEntity, updateReq);
        handleRelationships(existingEntity, updateReq, repository.getEntityClass());
        T savedEntity = save(repository, existingEntity);

        if (projectionClass != null) {
            try {
                return getById(repository, id, projectionClass);
            } catch (Exception e) {
                // If the projection fails, return the full entity
                return (D) savedEntity;
            }
        }
        return (D) savedEntity;
    }

    @SuppressWarnings("unchecked")
    public <T, D> Class<D> resolveProjectionClass(String projection, Class<T> entityClass) {
        if (projection != null && projectionRegistry.hasProjection(projection)) {
            return (Class<D>) projectionRegistry.getProjectionClass(projection);
        } else if (projectionRegistry.hasProjectionForEntity(entityClass)) {
            return (Class<D>) projectionRegistry.getProjectionForEntity(entityClass);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T, D> D projectWithNested(T entity, Class<D> projectionClass) {
        if (projectionClass == null || projectionClass == Object.class) {
            return (D) entity;
        }

        try {
            D projectedEntity = projectionHandler.project(entity, projectionClass, new HashSet<>());
            if (projectedEntity == null) {
                logger.warn("Projection returned null for entity of type {} to projection type {}",
                        entity.getClass().getName(), projectionClass.getName());
                return (D) entity;
            }

            return projectedEntity;
        } catch (Exception e) {
            logger.error("Failed to project entity of type {} to projection type {}: {}",
                    entity.getClass().getName(), projectionClass.getName(), e.getMessage());
            // If the projection fails, return the original entity
            return (D) entity;
        }
    }

    private <T> void handleRelationships(T entity, Map<String, Object> data, Class<T> entityClass) throws Exception {
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (!isRelationshipField(field)) {
                continue;
            }

            String fieldName = field.getName();
            Object value = data.get(fieldName + "Ids");

            if (value == null) {
                continue;
            }

            Class<?> relatedEntityClass = getRelationType(field);
            String relatedEntityName = relatedEntityClass.getSimpleName().toLowerCase();
            GenericRepository<?, ?> relatedRepository = getJpaRepository(relatedEntityName);

            if (Collection.class.isAssignableFrom(field.getType())) {
                handleCollectionRelationship(entity, field, value, relatedRepository);
            } else {
                handleSingleRelationship(entity, field, value, relatedRepository);
            }
        }
    }

    private boolean isRelationshipField(Field field) {
        return field.isAnnotationPresent(OneToMany.class) ||
                field.isAnnotationPresent(ManyToOne.class) ||
                field.isAnnotationPresent(ManyToMany.class) ||
                field.isAnnotationPresent(OneToOne.class);
    }

    @SuppressWarnings("unchecked")
    private <T> void handleCollectionRelationship(T entity, Field field, Object value, GenericRepository<?, ?> relatedRepository) throws Exception {
        Collection<?> relatedEntities;
        if (value instanceof List<?> ids) {
            relatedEntities = ((GenericRepository<Object, Object>) relatedRepository).findAllById((Iterable<Object>) ids);
        } else {
            Object id = objectMapper.convertValue(value, field.getType());
            relatedEntities = Collections.singletonList(((GenericRepository<Object, Object>) relatedRepository).findById(id).orElse(null));
        }

        if (field.getType().isAssignableFrom(Set.class)) {
            field.set(entity, new HashSet<>(relatedEntities));
        } else if (field.getType().isAssignableFrom(List.class)) {
            field.set(entity, new ArrayList<>(relatedEntities));
        } else {
            field.set(entity, relatedEntities);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void handleSingleRelationship(T entity, Field field, Object value, GenericRepository<?, ?> relatedRepository) throws Exception {
        Object id = objectMapper.convertValue(value, field.getType());
        Object relatedEntity = ((GenericRepository<Object, Object>) relatedRepository).findById(id).orElse(null);
        field.set(entity, relatedEntity);
    }

    private Class<?> getRelationType(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            return (Class<?>) genericType.getActualTypeArguments()[0];
        }
        return field.getType();
    }

    private Field findIdField(Class<?> entityClass) {
        Class<?> currentClass = entityClass;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(jakarta.persistence.Id.class) ||
                        field.isAnnotationPresent(jakarta.persistence.EmbeddedId.class)) {
                    return field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }
}
