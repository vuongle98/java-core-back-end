package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.User;

import java.util.Optional;

public interface UserRepository extends
        BaseRepository<User>,
        BaseQueryRepository<User> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
