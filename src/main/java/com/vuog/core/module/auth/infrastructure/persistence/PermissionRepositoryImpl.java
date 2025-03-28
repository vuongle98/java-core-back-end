package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.module.rest.domain.repository.GenericRepository;
import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.auth.domain.repository.PermissionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepositoryImpl extends GenericRepository<Permission, Long>, PermissionRepository {

    @Override
    default Class<Permission> getEntityClass() {
        return Permission.class;
    }
}
