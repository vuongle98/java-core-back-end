package com.vuog.core.module.user.application.specification;

import com.vuog.core.module.user.application.query.UserQuery;
import com.vuog.core.module.user.domain.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withFilter(UserQuery userQuery) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(userQuery.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + userQuery.getUsername() + "%"));
            }

            if (StringUtils.hasText(userQuery.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + userQuery.getEmail() + "%"));
            }

            if (StringUtils.hasText(userQuery.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + userQuery.getPhone() + "%"));
            }

            if (StringUtils.hasText(userQuery.getAddress())) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + userQuery.getAddress() + "%"));
            }

            if (StringUtils.hasText(userQuery.getRole())) {
                predicates.add(criteriaBuilder.like(root.get("role"), "%" + userQuery.getRole() + "%"));
            }

            if (StringUtils.hasText(userQuery.getStatus())) {
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + userQuery.getStatus() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
