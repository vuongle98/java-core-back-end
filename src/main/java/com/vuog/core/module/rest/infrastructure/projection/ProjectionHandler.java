package com.vuog.core.module.rest.infrastructure.projection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ProjectionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProjectionHandler.class);
    private final Map<ProjectionKey, Object> projectionCache = new ConcurrentHashMap<>();

    public <T> Map<String, Object> project(T entity, List<String> fields) {
        Map<String, Object> result = new HashMap<>();

        for (String fieldName : fields) {
            try {
                Object value = getFieldValue(entity, fieldName);
                result.put(fieldName, value);
            } catch (Exception e) {
                logger.warn("Failed to project field {}: {}", fieldName, e.getMessage());
                result.put(fieldName, null);
            }
        }

        return result;
    }

    public <T, D> D project(T entity, Class<D> projectionClass) {
        if (entity == null) {
            logger.warn("Cannot project null entity");
            return null;
        }

        if (projectionClass == null) {
            logger.warn("Cannot project to null projection class");
            return null;
        }

        try {
            // Check cache first
            ProjectionKey key = new ProjectionKey(entity, projectionClass);
            @SuppressWarnings("unchecked")
            D cachedResult = (D) projectionCache.get(key);
            if (cachedResult != null && !isProjectionChanged(entity, cachedResult, projectionClass)) {
                return cachedResult;
            }

            logger.debug("Starting projection from {} to {}", entity.getClass().getName(), projectionClass.getName());

            D result;
            if (projectionClass.isInterface()) {
                result = createInterfaceProxy(entity, projectionClass);
            } else {
                result = createClassInstance(entity, projectionClass);
            }

            if (result != null) {
                projectionCache.put(key, result);
                return result;
            }

            logger.warn("Failed to create projection for entity of type {} to projection type {}",
                    entity.getClass().getName(), projectionClass.getName());
            return null;
        } catch (Exception e) {
            logger.error("Error projecting entity of type {} to projection type {}: {}",
                    entity.getClass().getName(), projectionClass.getName(), e.getMessage(), e);
            throw new RuntimeException("Error projecting entity to " + projectionClass.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T, D> D createInterfaceProxy(T entity, Class<D> projectionClass) {
        Map<String, Object> values = new HashMap<>();
        boolean hasValues = false;

        for (Method method : projectionClass.getMethods()) {
            String methodName = method.getName();
            if (!methodName.startsWith("get") || methodName.length() <= 3) {
                continue;
            }

            // Safely convert method name to field name
            String fieldName = methodName.substring(3);
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

            Object value = getFieldValue(entity, fieldName);
            if (value != null) {
                hasValues = true;
                Class<?> returnType = method.getReturnType();

                // Handle collection types
                if (Collection.class.isAssignableFrom(returnType)) {
                    if (value instanceof Collection<?> collection) {
                        Collection<Object> projectedCollection;

                        // Create the appropriate collection type
                        if (returnType.isAssignableFrom(Set.class)) {
                            projectedCollection = new HashSet<>();
                        } else if (returnType.isAssignableFrom(List.class)) {
                            projectedCollection = new ArrayList<>();
                        } else {
                            projectedCollection = new ArrayList<>();
                        }

                        // Get the element type from the method's generic return type
                        java.lang.reflect.Type genericReturnType = method.getGenericReturnType();
                        if (genericReturnType instanceof ParameterizedType paramType) {
                            java.lang.reflect.Type[] typeArguments = paramType.getActualTypeArguments();
                            if (typeArguments.length > 0) {
                                java.lang.reflect.Type elementType = typeArguments[0];
                                if (elementType instanceof Class<?> elementClass) {
                                    for (Object element : collection) {
                                        if (element != null) {
                                            Object projectedElement = project(element, elementClass);
                                            if (projectedElement != null) {
                                                projectedCollection.add(projectedElement);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        value = projectedCollection;
                    }
                } else {
                    value = projectValue(value, returnType);
                }

                values.put(methodName, value);
                logger.debug("Stored projected value for {}: {}", methodName, value);
            }
        }

        if (!hasValues) {
            logger.warn("No values found for entity of type {} when creating proxy for {}",
                    entity.getClass().getName(), projectionClass.getName());
            return null;
        }

        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                return values.get(methodName);
            } else if (methodName.startsWith("set")) {
                // Store the value for the corresponding getter
                String getterName = "get" + methodName.substring(3);
                values.put(getterName, args[0]);
                return null;
            }
            return switch (methodName) {
                case "toString" -> "Proxy for " + projectionClass.getName();
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                default -> null;
            };
        };

        try {
            ClassLoader classLoader = projectionClass.getClassLoader();
            if (classLoader == null) {
                classLoader = entity.getClass().getClassLoader();
            }

            if (classLoader == null) {
                logger.error("No class loader available for proxy creation");
                return null;
            }

            return (D) Proxy.newProxyInstance(classLoader, new Class<?>[]{projectionClass}, handler);
        } catch (Exception e) {
            logger.error("Failed to create proxy for {}: {}", projectionClass.getName(), e.getMessage(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T, D> D createClassInstance(T entity, Class<D> projectionClass) {
        try {
            // Handle primitive types and their wrappers
            if (isPrimitiveOrWrapper(projectionClass)) {
                Object value = getFieldValue(entity, projectionClass.getSimpleName().toLowerCase());
                return (D) convertToType(value, projectionClass);
            }

            // Try to create instance with no-args constructor first
            try {
                D instance = projectionClass.getDeclaredConstructor().newInstance();
                populateInstance(instance, entity);
                return instance;
            } catch (Exception e) {
                // If no-args constructor fails, try to find a constructor with matching parameters
                Constructor<?>[] constructors = projectionClass.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    try {
                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        Object[] args = new Object[paramTypes.length];

                        // Try to get values for constructor parameters
                        for (int i = 0; i < paramTypes.length; i++) {
                            String paramName = getParameterName(constructor, i);
                            Object value = getFieldValue(entity, paramName);
                            if (value != null) {
                                args[i] = convertToType(value, paramTypes[i]);
                            }
                        }

                        D instance = (D) constructor.newInstance(args);
                        populateInstance(instance, entity);
                        return instance;
                    } catch (Exception ex) {
                        // Try next constructor
                        continue;
                    }
                }
                throw new RuntimeException("No suitable constructor found for " + projectionClass.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to create instance for {}: {}", projectionClass.getName(), e.getMessage());
            return null;
        }
    }

    private <T, D> void populateInstance(D instance, T entity) {
        for (Method method : instance.getClass().getMethods()) {
            String methodName = method.getName();
            if (!methodName.startsWith("set") || methodName.length() <= 3) {
                continue;
            }

            // Safely convert method name to field name
            String fieldName = methodName.substring(3);
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

            Object value = getFieldValue(entity, fieldName);
            if (value != null) {
                try {
                    value = projectValue(value, method.getParameterTypes()[0]);
                    method.invoke(instance, value);
                } catch (Exception e) {
                    logger.warn("Failed to set value for field {}: {}", fieldName, e.getMessage());
                }
            }
        }
    }

    private String getParameterName(Constructor<?> constructor, int index) {
        try {
            // Try to get parameter name from annotations
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length > index) {
                return parameters[index].getName();
            }
        } catch (Exception e) {
            // Ignore if parameter names are not available
        }

        // Fallback to a default name based on index
        return "arg" + index;
    }

    private Object projectValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        try {
            // Handle primitive types and their wrappers
            if (isPrimitiveOrWrapper(targetType)) {
                return convertToType(value, targetType);
            }

            if (value instanceof Collection) {
                return projectCollection((Collection<?>) value, targetType);
            } else if (targetType.isInterface()) {
                return project(value, targetType);
            } else if (Collection.class.isAssignableFrom(targetType)) {
                return projectCollection((Collection<?>) value, targetType);
            } else {
                try {
                    return project(value, targetType);
                } catch (Exception e) {
                    try {
                        return targetType.cast(value);
                    } catch (ClassCastException ex) {
                        try {
                            Object newInstance = targetType.getDeclaredConstructor().newInstance();
                            copyProperties(value, newInstance);
                            return newInstance;
                        } catch (Exception exc) {
                            logger.warn("Failed to handle value: {}", exc.getMessage());
                            return null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to project value: {}", e.getMessage());
            return null;
        }
    }

    private Object convertToType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        try {
            // Handle primitive types and their wrappers
            if (targetType.isPrimitive()) {
                if (targetType == boolean.class) {
                    return Boolean.parseBoolean(value.toString());
                } else if (targetType == byte.class) {
                    return Byte.parseByte(value.toString());
                } else if (targetType == char.class) {
                    return value.toString().charAt(0);
                } else if (targetType == double.class) {
                    return Double.parseDouble(value.toString());
                } else if (targetType == float.class) {
                    return Float.parseFloat(value.toString());
                } else if (targetType == int.class) {
                    return Integer.parseInt(value.toString());
                } else if (targetType == long.class) {
                    return Long.parseLong(value.toString());
                } else if (targetType == short.class) {
                    return Short.parseShort(value.toString());
                }
            } else {
                // Handle wrapper types and special types
                if (targetType == Boolean.class) {
                    return Boolean.valueOf(value.toString());
                } else if (targetType == Byte.class) {
                    return Byte.valueOf(value.toString());
                } else if (targetType == Character.class) {
                    return value.toString().charAt(0);
                } else if (targetType == Double.class) {
                    return Double.valueOf(value.toString());
                } else if (targetType == Float.class) {
                    return Float.valueOf(value.toString());
                } else if (targetType == Integer.class) {
                    return Integer.valueOf(value.toString());
                } else if (targetType == Long.class) {
                    return Long.valueOf(value.toString());
                } else if (targetType == Short.class) {
                    return Short.valueOf(value.toString());
                } else if (targetType == String.class) {
                    return value.toString();
                } else if (targetType == java.time.Instant.class) {
                    if (value instanceof java.time.Instant) {
                        return value;
                    }
                    return java.time.Instant.parse(value.toString());
                } else if (targetType == java.time.LocalDate.class) {
                    if (value instanceof java.time.LocalDate) {
                        return value;
                    }
                    return java.time.LocalDate.parse(value.toString());
                } else if (targetType == java.time.LocalTime.class) {
                    if (value instanceof java.time.LocalTime) {
                        return value;
                    }
                    return java.time.LocalTime.parse(value.toString());
                }
            }
            return value;
        } catch (Exception e) {
            logger.warn("Failed to convert value {} to type {}: {}", value, targetType.getName(), e.getMessage());
            return null;
        }
    }

    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Boolean.class ||
                type == Byte.class ||
                type == Character.class ||
                type == Double.class ||
                type == Float.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Short.class ||
                type == String.class ||
                type == LocalDate.class ||
                type == LocalTime.class ||
                type == Date.class ||
                type == Time.class ||
                type == Instant.class;
    }

    private Class<?> getCollectionElementType(Class<?> collectionType) {
        try {
            if (collectionType.isArray()) {
                return collectionType.getComponentType();
            }

            if (Collection.class.isAssignableFrom(collectionType)) {
                // First try to get the type from the generic superclass
                java.lang.reflect.Type genericType = collectionType.getGenericSuperclass();
                if (genericType instanceof ParameterizedType) {
                    java.lang.reflect.Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                    if (typeArguments.length > 0) {
                        java.lang.reflect.Type elementType = typeArguments[0];
                        if (elementType instanceof Class) {
                            return (Class<?>) elementType;
                        } else if (elementType instanceof ParameterizedType nestedType) {
                            // Handle nested generic types
                            if (nestedType.getRawType() instanceof Class) {
                                return (Class<?>) nestedType.getRawType();
                            }
                        }
                    }
                }

                // If no generic type found, try to get it from the interface
                for (java.lang.reflect.Type interfaceType : collectionType.getGenericInterfaces()) {
                    if (interfaceType instanceof ParameterizedType paramType) {
                        if (paramType.getRawType() == Collection.class) {
                            java.lang.reflect.Type[] typeArguments = paramType.getActualTypeArguments();
                            if (typeArguments.length > 0) {
                                java.lang.reflect.Type elementType = typeArguments[0];
                                if (elementType instanceof Class) {
                                    return (Class<?>) elementType;
                                } else if (elementType instanceof ParameterizedType nestedType) {
                                    if (nestedType.getRawType() instanceof Class) {
                                        return (Class<?>) nestedType.getRawType();
                                    }
                                }
                            }
                        }
                    }
                }

                // If still no type found, try to get it from the iterator
                Method iteratorMethod = collectionType.getMethod("iterator");
                Class<?> iteratorType = iteratorMethod.getReturnType();
                Method nextMethod = iteratorType.getMethod("next");
                return nextMethod.getReturnType();
            }

            return Object.class;
        } catch (Exception e) {
            logger.warn("Failed to get collection element type: {}", e.getMessage());
            return Object.class;
        }
    }

    private <T> Object projectCollection(Collection<T> collection, Class<?> targetType) {
        if (collection == null) {
            return null;
        }

        Collection<T> standardCollection = collection.getClass().getName().contains("org.hibernate.collection")
                ? new ArrayList<>(collection)
                : collection;

        Class<?> elementType = getCollectionElementType(targetType);
        if (elementType == null || elementType == Object.class) {
            // Create the appropriate collection type based on target type
            if (targetType.isAssignableFrom(Set.class)) {
                return new HashSet<>(standardCollection);
            } else if (targetType.isAssignableFrom(List.class)) {
                return new ArrayList<>(standardCollection);
            } else {
                return standardCollection;
            }
        }

        try {
            Collection<Object> resultCollection;
            if (targetType.isAssignableFrom(Set.class)) {
                resultCollection = new HashSet<>();
            } else if (targetType.isAssignableFrom(List.class)) {
                resultCollection = new ArrayList<>();
            } else {
                resultCollection = new ArrayList<>();
            }

            for (T item : standardCollection) {
                if (item != null) {
                    Object projectedItem = projectValue(item, elementType);
                    if (projectedItem != null) {
                        resultCollection.add(projectedItem);
                    }
                }
            }

            return resultCollection;
        } catch (Exception e) {
            logger.error("Failed to project collection: {}", e.getMessage(), e);
            // Fallback to appropriate collection type
            if (targetType.isAssignableFrom(Set.class)) {
                return new HashSet<>(standardCollection);
            } else if (targetType.isAssignableFrom(List.class)) {
                return new ArrayList<>(standardCollection);
            } else {
                return standardCollection;
            }
        }
    }

    private void copyProperties(Object source, Object target) {
        try {
            for (Method getter : source.getClass().getMethods()) {
                if (getter.getName().startsWith("get")) {
                    String setterName = "set" + getter.getName().substring(3);
                    try {
                        Method setter = target.getClass().getMethod(setterName, getter.getReturnType());
                        Object value = getter.invoke(source);
                        if (value != null) {
                            setter.invoke(target, value);
                        }
                    } catch (Exception e) {
                        // Skip if setter doesn't exist or types don't match
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to copy properties: {}", e.getMessage());
        }
    }

    private <T> Object getFieldValue(T entity, String fieldName) {
        if (entity == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        try {
            // First try to get the value through a getter method
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = findMethodInClassHierarchy(entity.getClass(), getterName);
            if (getter != null) {
                getter.setAccessible(true);
                return getter.invoke(entity);
            }

            // If no getter exists, try to get the field directly
            Field field = findFieldInClassHierarchy(entity.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(entity);
            }

            // If field is not found, try to handle it as a nested property
            if (fieldName.contains(".")) {
                String[] parts = fieldName.split("\\.");
                Object currentValue = entity;

                for (String part : parts) {
                    if (currentValue == null || part == null || part.isEmpty()) {
                        return null;
                    }
                    currentValue = getFieldValue(currentValue, part);
                }
                return currentValue;
            }

            logger.warn("Field or getter not found for {} in class {}", fieldName, entity.getClass().getName());
            return null;
        } catch (Exception e) {
            logger.warn("Failed to get value for {}: {}", fieldName, e.getMessage());
            return null;
        }
    }

    private Method findMethodInClassHierarchy(Class<?> clazz, String methodName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    private Field findFieldInClassHierarchy(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    private boolean isProjectionChanged(Object entity, Object cachedProjection, Class<?> projectionClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // hỗ trợ Instant, LocalDateTime, etc.
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // Dựng projection hiện tại từ entity
            Object currentProjection = projectionClass.isInterface()
                    ? createInterfaceProxy(entity, projectionClass)
                    : createClassInstance(entity, projectionClass);

            // So sánh JSON
            String json1 = mapper.writeValueAsString(currentProjection);
            String json2 = mapper.writeValueAsString(cachedProjection);

            return !json1.equals(json2); // nếu khác => cần cập nhật cache
        } catch (JsonProcessingException e) {
            logger.warn("Error comparing projection JSON", e);
            return true;
        }
    }

    private static class ProjectionKey {
        private final Object entity;
        private final Class<?> projectionClass;

        public ProjectionKey(Object entity, Class<?> projectionClass) {
            this.entity = entity;
            this.projectionClass = projectionClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProjectionKey that = (ProjectionKey) o;
            if (deepEquals(this.entity, that.entity)) return true;
            return Objects.equals(entity, that.entity) && Objects.equals(projectionClass, that.projectionClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(entity, projectionClass);
        }

        private boolean deepEquals(Object o1, Object o2) {
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                String json1 = mapper.writeValueAsString(o1);
                String json2 = mapper.writeValueAsString(o2);
                return json1.equals(json2);
            } catch (JsonProcessingException e) {
                logger.warn("Error comparing entities for deepEquals", e);
                return false;
            }
        }
    }
}


