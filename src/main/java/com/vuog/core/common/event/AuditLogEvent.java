package com.vuog.core.common.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AuditLogEvent implements DomainEvent {

    private Map<String, Object> entity;

    @Override
    public String eventName() {
        return "event.AuditLogEvent";
    }

    @Override
    public String toPayload() {
        return entity.toString();
    }

    @Override
    public String key() {
        return null;
    }
}
