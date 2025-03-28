package com.vuog.core.module.auth.application.specification;

import com.vuog.core.module.auth.application.query.RoleQuery;
import com.vuog.core.module.auth.domain.model.Role;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification {

    public static Specification<Role> withFilter(RoleQuery roleQuery) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(roleQuery.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + roleQuery.getName() + "%"));
            }

            if (StringUtils.hasText(roleQuery.getCode())) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + roleQuery.getCode() + "%"));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
