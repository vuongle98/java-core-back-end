package com.vuog.core.module.auth.application.specification;

import com.vuog.core.module.auth.application.query.UserQuery;
import com.vuog.core.module.auth.domain.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserSpecification {

    public static Specification<User> withFilter(UserQuery userQuery) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(userQuery.getId())) {
                predicates.add(criteriaBuilder.equal(root.get("id"), userQuery.getId()));
            }

            if (Objects.nonNull(userQuery.getSearch())) {
                predicates.add(criteriaBuilder.or(
                                criteriaBuilder.like(root.get("username"), "%" + userQuery.getSearch() + "%"),
                                criteriaBuilder.like(root.get("email"), "%" + userQuery.getSearch() + "%")
                            )
                );
            }

            if (Objects.nonNull(userQuery.getRolesIds())) {
                predicates.add(root.get("roles").get("id").in(userQuery.getRolesIds()));
            }

            if (StringUtils.hasText(userQuery.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + userQuery.getUsername() + "%"));
            }

            if (StringUtils.hasText(userQuery.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + userQuery.getEmail() + "%"));
            }

            if (StringUtils.hasText(userQuery.getRole())) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("roles").get("code"), "%" + userQuery.getRole() + "%"),
                        criteriaBuilder.like(root.get("roles").get("name"), "%" + userQuery.getRole() + "%")
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
