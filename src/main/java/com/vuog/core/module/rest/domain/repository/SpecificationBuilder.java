package com.vuog.core.module.rest.domain.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecificationBuilder {

    public static <T> Specification<T> build(Map<String, String> filters) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
