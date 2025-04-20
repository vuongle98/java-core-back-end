package com.vuog.core.module.rest.domain.specification;

import com.vuog.core.module.rest.shared.config.EntitySearchConfig;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                // Get the Java type of the field
                Class<?> fieldType = root.get(key).getJavaType();

                if (fieldType == String.class) {
                    predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    predicates.add(criteriaBuilder.equal(root.get(key), Boolean.valueOf(value)));
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    // Try to parse as number (Integer, Long, Double, etc.)
                    // You can customize or add more if needed
                    if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.equal(root.get(key), Integer.valueOf(value)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.equal(root.get(key), Long.valueOf(value)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.equal(root.get(key), Double.valueOf(value)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.equal(root.get(key), Float.valueOf(value)));
                    } else {
                        // Default as string equality fallback
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                    }
                } else if (fieldType.isEnum()) {
                    // Try to parse enums by name
                    Object enumValue = Enum.valueOf((Class<Enum>)fieldType, value);
                    predicates.add(criteriaBuilder.equal(root.get(key), enumValue));
                } else {
                    // Default: use equality
                    predicates.add(criteriaBuilder.equal(root.get(key), value));
                }
            });


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
