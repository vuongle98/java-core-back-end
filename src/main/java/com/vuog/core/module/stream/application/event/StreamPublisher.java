package com.vuog.core.module.stream.application.event;

import com.vuog.core.common.event.StreamMessage;

/**
 * Interface for publishing stream messages
 */
public interface StreamPublisher {

    /**
     * Publish a message to a stream
     * @param key The message key (can be null)
     * @param event The message to publish
     */
    void publish(String key, StreamMessage event);
}
