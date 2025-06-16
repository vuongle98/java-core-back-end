package com.vuog.core.common.event;

/**
 * Interface for messages that can be published to a message stream (e.g. Kafka)
 */
public interface StreamMessage {

    /**
     * Get the topic name to publish this message to
     * @return The topic name
     */
    String topic();

    /**
     * Convert this message to a string payload for publishing
     * @return The serialized message payload
     */
    String toPayload();
}
