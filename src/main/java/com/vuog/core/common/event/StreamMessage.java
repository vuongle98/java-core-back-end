package com.vuog.core.common.event;

public interface StreamMessage {

    String topic();
    String toPayload();
}
