package com.vuog.core.module.user.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.user.domain.model.User;

public interface UserRepository extends
        BaseRepository<User>,
        BaseQueryRepository<User> {

}
