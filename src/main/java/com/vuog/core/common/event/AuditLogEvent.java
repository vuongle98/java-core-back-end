package com.vuog.core.common.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.common.util.ObjectMappingUtil;
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
    public String key() {
        return null;
    }


    @Override
    public String eventName() {
        return "log.auditLog";
    }

    @Override
    public String toPayload() {
        Event<Map<String, Object>> event = new Event<>(EventType.CREATE, entity);
        return ObjectMappingUtil.writeAsString(event);
    }
}
