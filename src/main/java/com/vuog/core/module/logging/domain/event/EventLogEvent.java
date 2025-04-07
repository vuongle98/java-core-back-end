package com.vuog.core.module.logging.domain.event;

import com.vuog.core.common.event.DomainEvent;
import com.vuog.core.module.logging.domain.model.AuditLog;
import com.vuog.core.module.logging.domain.model.EventLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class EventLogEvent implements DomainEvent {

    private EventLog eventLog;

    @Override
    public String eventName() {
        return "event.EventLogEvent";
    }

    @Override
    public String toPayload() {
        return eventLog.toString();
    }

    @Override
    public String key() {
        return null;
    }
}
