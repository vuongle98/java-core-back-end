package com.vuog.core.module.logging.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.logging.domain.model.AuditLog;

public interface AuditLogRepository extends
        BaseRepository<AuditLog>,
        BaseQueryRepository<AuditLog> {
}
