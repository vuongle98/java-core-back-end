package com.vuog.core.common.event;

public interface DomainEvent {

    String eventName();
    String toPayload();
    String key();
}
