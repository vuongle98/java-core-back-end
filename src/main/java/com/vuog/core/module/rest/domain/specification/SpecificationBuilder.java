package com.vuog.core.module.rest.domain.specification;

import com.vuog.core.module.rest.shared.config.EntitySearchConfig;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpecificationBuilder {

    /**
     * @param filters typical filters ("status", "isRole", etc.)
     * @param entityClass the entity class to find searchable fields
     */

    public static <T> Specification<T> build(Map<String, String> filters, Class<T> entityClass) {
        List<String> searchableFields = EntitySearchConfig.getSearchableFields(entityClass);
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Handle global search
            if (filters.containsKey("search") && !searchableFields.isEmpty()) {
                String searchValue = filters.get("search");
                List<Predicate> orPredicates = new ArrayList<>();
                for (String field : searchableFields) {
                    orPredicates.add(criteriaBuilder.like(root.get(field), "%" + searchValue + "%"));
                }
                if (!orPredicates.isEmpty())
                    predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
            }


            filters.forEach((key, value) -> {
                if ("search".equals(key)) return;

                if (key.endsWith("Ids")) {
                    String relationField = key.substring(0, key.length() - 3);

                    if (hasField(entityClass, relationField)) {
                        try {
                            Join<Object, Object> join = root.join(relationField, JoinType.LEFT);

                            List<Long> ids = Arrays.stream(value.split(","))
                                    .map(String::trim)
                                    .map(Long::valueOf)
                                    .toList();
                            predicates.add(join.get("id").in(ids));
                        } catch (IllegalArgumentException e) {
                            // case field not support
                        }
                    }
                }

                if (!hasField(entityClass, key)) return;

                // Get the Java type of the field
                Class<?> fieldType = root.get(key).getJavaType();

                if (fieldType == String.class) {
                    predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    predicates.add(criteriaBuilder.equal(root.get(key), Boolean.valueOf(value)));
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    // Try to parse as number (Integer, Long, Double, etc.)
                    // You can customize or add more if needed
                    predicates.add(criteriaBuilder.equal(root.get(key), parseNumber(value, fieldType)));
                } else if (fieldType.isEnum()) {
                    // Try to parse enums by name
                    Object enumValue = Enum.valueOf((Class<Enum>) fieldType, value);
                    predicates.add(criteriaBuilder.equal(root.get(key), enumValue));
                } else {
                    // Default: use equality
                    predicates.add(criteriaBuilder.equal(root.get(key), value));
                }
            });


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static boolean hasField(Class<?> clazz, String fieldName) {
        try {
            Field field = getField(clazz, fieldName);
            return field != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static Object parseNumber(String value, Class<?> type) {
        if (type == Integer.class) return Integer.valueOf(value);
        if (type == Long.class) return Long.valueOf(value);
        if (type == Double.class) return Double.valueOf(value);
        if (type == Float.class) return Float.valueOf(value);
        return null;
    }
}
