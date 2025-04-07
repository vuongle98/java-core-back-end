package com.vuog.core.module.logging.infrastructure.persistence;

import com.vuog.core.module.logging.domain.model.UserRequestLog;
import com.vuog.core.module.logging.domain.repository.UserRequestLogRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;

public interface JpaUserRequestLogRepository extends
        GenericRepository<UserRequestLog, Long>, UserRequestLogRepository {

    default Class<UserRequestLog> getEntityClass() {
        return UserRequestLog.class;
    }
}
