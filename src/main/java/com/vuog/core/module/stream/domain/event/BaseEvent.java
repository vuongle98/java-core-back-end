package com.vuog.core.module.stream.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {
    private String eventId;
    private String eventType;
    private Instant timestamp;
    private String source;

    /**
     * Returns the topic name for this event
     */
    public abstract String getTopic();

    /**
     * Initialization for common event properties
     */
    public void init() {
        if (eventId == null) {
            eventId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (eventType == null) {
            eventType = this.getClass().getSimpleName();
        }
    }
}
