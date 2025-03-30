package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.Permission;

import java.util.Optional;

public interface PermissionRepository extends
        BaseRepository<Permission>,
        BaseQueryRepository<Permission> {

    Optional<Permission> findByName(String name);
    Optional<Permission> findByCode(String code);

}
