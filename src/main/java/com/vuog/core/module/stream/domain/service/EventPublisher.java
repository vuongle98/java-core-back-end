package com.vuog.core.module.stream.domain.service;

import com.vuog.core.module.stream.domain.event.BaseEvent;

/**
 * Domain service interface for publishing events
 */
public interface EventPublisher {

    /**
     * Publish an event to the messaging system
     * @param event The event to publish
     */
    void publish(BaseEvent event);

    /**
     * Publish an event to the messaging system with a specific routing key
     * @param routingKey The routing key for message delivery
     * @param event The event to publish
     */
    void publish(String routingKey, BaseEvent event);
}
