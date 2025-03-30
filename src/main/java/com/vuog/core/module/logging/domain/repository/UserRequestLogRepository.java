package com.vuog.core.module.logging.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.logging.domain.model.UserRequestLog;

public interface UserRequestLogRepository extends
        BaseRepository<UserRequestLog>,
        BaseQueryRepository<UserRequestLog> {
}
