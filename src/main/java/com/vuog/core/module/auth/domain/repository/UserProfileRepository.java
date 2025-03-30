package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends
        BaseRepository<UserProfile>, BaseQueryRepository<UserProfile> {

    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUser_Id(Long userId);
}
