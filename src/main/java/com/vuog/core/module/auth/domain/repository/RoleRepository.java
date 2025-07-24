package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.common.repository.OwnableRepository;
import com.vuog.core.module.auth.domain.model.Role;

import java.util.Optional;

public interface RoleRepository extends
        BaseRepository<Role>,
        BaseQueryRepository<Role> {

    Optional<Role> findByName(String name);
    Optional<Role> findByCode(String code);

    boolean existsByChildRolesContaining(Role role);
}
