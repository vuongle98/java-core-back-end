package com.vuog.core.module.rest.infrastructure.projection;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProjectionRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ProjectionRegistry.class);
    private static final String CLASS_PATH_PATTERN = "classpath*:%s/**/*.class";
    private final Map<String, Class<?>> projectionMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<?>> entityProjectionMap = new ConcurrentHashMap<>();
    private final PathMatchingResourcePatternResolver resourceResolver;
    private final MetadataReaderFactory metadataReaderFactory;
    @Value("${app.base-packages:com.vuog.core.module}")
    private String[] basePackages;

    public ProjectionRegistry() {
        this.resourceResolver = new PathMatchingResourcePatternResolver();
        this.metadataReaderFactory = new SimpleMetadataReaderFactory();
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing ProjectionRegistry with base packages: {}", Arrays.toString(basePackages));
        initialize();
        logRegistryState();
    }

    public void initialize() {
        try {
            for (String basePackage : basePackages) {
                scanForProjectionInterfaces(basePackage);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize ProjectionRegistry", e);
            throw new RuntimeException("Failed to initialize ProjectionRegistry", e);
        }
    }

    private void scanForProjectionInterfaces(String basePackage) throws IOException, ClassNotFoundException {
        String packageSearchPath = String.format(CLASS_PATH_PATTERN, basePackage.replace('.', '/'));
        logger.debug("Scanning for projection interfaces in: {}", packageSearchPath);

        Resource[] resources = resourceResolver.getResources(packageSearchPath);
        logger.info("Found {} resources in package: {}", resources.length, basePackage);

        for (Resource resource : resources) {
            try {
                processResource(resource);
            } catch (Exception e) {
                logger.warn("Failed to process resource: {}", resource.getDescription(), e);
            }
        }
    }

    private void processResource(Resource resource) throws IOException, ClassNotFoundException {
        if (!resource.isReadable()) {
            logger.debug("Skipping non-readable resource: {}", resource.getDescription());
            return;
        }

        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        String className = metadataReader.getClassMetadata().getClassName();

        // Skip if not in our target packages
        if (!isInTargetPackages(className)) {
            return;
        }

        Class<?> clazz = loadClass(className);
        if (clazz == null) {
            return;
        }

        if (clazz.isInterface() && clazz.isAnnotationPresent(ProjectionDefinition.class)) {
            registerProjection(clazz);
        }
    }

    private boolean isInTargetPackages(String className) {
        return Arrays.stream(basePackages)
                .anyMatch(basePackage -> className.startsWith(basePackage));
    }

    private Class<?> loadClass(String className) {
        try {
            return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            logger.warn("Failed to load class: {}", className, e);
            return null;
        }
    }

    private void registerProjection(Class<?> projectionClass) {
        ProjectionDefinition annotation = projectionClass.getAnnotation(ProjectionDefinition.class);
        String name = annotation.name().isEmpty() ? projectionClass.getSimpleName() : annotation.name();

        // Check for duplicate projection names
        if (projectionMap.containsKey(name)) {
            logger.warn("Duplicate projection name found: {}. Existing: {}, New: {}",
                    name, projectionMap.get(name).getName(), projectionClass.getName());
        }

        projectionMap.put(name, projectionClass);

        // Register entity mappings
        for (Class<?> entityType : annotation.types()) {
            if (entityProjectionMap.containsKey(entityType)) {
                logger.warn("Duplicate entity mapping found for: {}. Existing: {}, New: {}",
                        entityType.getName(), entityProjectionMap.get(entityType).getName(), projectionClass.getName());
            }
            entityProjectionMap.put(entityType, projectionClass);
        }

        logger.info("Registered projection: {} -> {}", name, projectionClass.getName());
    }

    private void logRegistryState() {
        logger.info("ProjectionRegistry initialized:");
        logger.info("Total projections registered: {}", projectionMap.size());
        logger.info("Total entity-projection mappings: {}", entityProjectionMap.size());

        if (logger.isDebugEnabled()) {
            projectionMap.forEach((name, clazz) ->
                    logger.debug("Projection: {} -> {}", name, clazz.getName()));

            entityProjectionMap.forEach((entity, projection) ->
                    logger.debug("Entity-Projection mapping: {} -> {}", entity.getName(), projection.getName()));
        }
    }

    public boolean hasProjection(String projectionName) {
        return projectionMap.containsKey(projectionName);
    }

    public Class<?> getProjectionClass(String projectionName) {
        Class<?> projectionClass = projectionMap.get(projectionName);
        if (projectionClass == null) {
            logger.warn("Projection not found: {}", projectionName);
        }
        return projectionClass;
    }

    public boolean hasProjectionForEntity(Class<?> entityType) {
        return entityProjectionMap.containsKey(entityType);
    }

    public Class<?> getProjectionForEntity(Class<?> entityType) {
        Class<?> projectionClass = entityProjectionMap.get(entityType);
        if (projectionClass == null) {
            logger.warn("No projection found for entity: {}", entityType.getName());
        }
        return projectionClass;
    }

    public Map<String, Method> getProjectionMethods(String projectionName) {
        Class<?> projectionClass = getProjectionClass(projectionName);
        if (projectionClass == null) {
            return null;
        }

        Map<String, Method> methods = new ConcurrentHashMap<>();
        for (Method method : projectionClass.getMethods()) {
            if (method.getParameterCount() == 0 && method.getName().startsWith("get")) {
                methods.put(method.getName(), method);
            }
        }
        return methods;
    }

    public Set<String> getAvailableProjections() {
        return projectionMap.keySet();
    }

    public Set<Class<?>> getAvailableEntityTypes() {
        return entityProjectionMap.keySet();
    }
}
