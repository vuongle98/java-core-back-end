package com.vuog.core.module.stream.application.event;

import com.vuog.core.common.event.StreamMessage;

public interface StreamPublisher {

    void publish(String key, StreamMessage message);

}
