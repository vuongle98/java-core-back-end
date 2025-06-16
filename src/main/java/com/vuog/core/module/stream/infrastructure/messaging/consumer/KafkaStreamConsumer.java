package com.vuog.core.module.stream.infrastructure.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.module.stream.domain.event.EntityChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for Kafka stream messages
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaStreamConsumer {

    private final ObjectMapper objectMapper;

    /**
     * Consume entity change events
     */
    @KafkaListener(topics = "entity.changes", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEntityChanges(String message) {
        try {
            EntityChangedEvent event = objectMapper.readValue(message, EntityChangedEvent.class);
            log.info("Received entity change event: {} - {}", event.getEntityType(), event.getChangeType());
            // Process the event
        } catch (Exception e) {
            log.error("Error consuming entity change event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consume log events
     */
    @KafkaListener(topics = "log.general", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeLogEvents(String message) {
        try {
            log.info("Received log event: {}", message);
            // Process the log event
        } catch (Exception e) {
            log.error("Error consuming log event: {}", e.getMessage(), e);
        }
    }

    /**
     * Example of consuming a specific entity's events
     */
    @KafkaListener(topics = "entity.user.created", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserCreatedEvents(String message) {
        try {
            log.info("New user created: {}", message);
            // Process the user creation event
        } catch (Exception e) {
            log.error("Error consuming user created event: {}", e.getMessage(), e);
        }
    }
}