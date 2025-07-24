package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.common.repository.OwnableRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositoryImpl extends
        GenericRepository<Role, Long>,
        RoleRepository,
        OwnableRepository<Role, Long> {
    @Override
    default Class<Role> getEntityClass() {
        return Role.class;
    }

    boolean existsByIdAndCreatedBy(Long id, String createdBy);
}
