package com.vuog.core.module.stream.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.module.stream.domain.event.EntityChangedEvent;
import com.vuog.core.module.stream.domain.event.EntityChangedEvent.ChangeType;
import com.vuog.core.module.stream.domain.service.EntityChangeTracker;
import com.vuog.core.module.stream.domain.service.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * Kafka implementation of EntityChangeTracker
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEntityChangeTracker implements EntityChangeTracker {

    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public void trackChange(Object entity, ChangeType changeType, String actor) {
        try {
            String entityType = entity.getClass().getSimpleName();
            String entityId = extractEntityId(entity);
            String entityData = objectMapper.writeValueAsString(entity);

            EntityChangedEvent event = EntityChangedEvent.builder()
                    .source("system")
                    .entityType(entityType)
                    .entityId(entityId)
                    .changeType(changeType)
                    .entityData(entityData)
                    .actor(actor)
                    .build();

            String routingKey = entityType + "-" + entityId;
            eventPublisher.publish(routingKey, event);

        } catch (Exception e) {
            log.error("Failed to track entity change: {}", e.getMessage(), e);
        }
    }

    /**
     * Extract the ID from an entity using reflection
     */
    private String extractEntityId(Object entity) {
        try {
            // First try getId method
            try {
                Method getIdMethod = entity.getClass().getMethod("getId");
                Object id = getIdMethod.invoke(entity);
                return id != null ? id.toString() : "unknown";
            } catch (NoSuchMethodException e) {
                // Try id field
                try {
                    java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object id = idField.get(entity);
                    return id != null ? id.toString() : "unknown";
                } catch (NoSuchFieldException ex) {
                    return "unknown";
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract ID from entity: {}", e.getMessage());
            return "unknown";
        }
    }
}
