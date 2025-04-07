package com.vuog.core.module.logging.domain.event;

import com.vuog.core.common.event.DomainEvent;
import com.vuog.core.module.logging.domain.model.AuditLog;
import com.vuog.core.module.logging.domain.model.UserRequestLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AuditLogEvent implements DomainEvent {

    private AuditLog auditLog;

    @Override
    public String eventName() {
        return "event.AuditLogEvent";
    }

    @Override
    public String toPayload() {
        return auditLog.toString();
    }

    @Override
    public String key() {
        return null;
    }
}
