package com.vuog.core.common.event;

public record Event<T>(EventType eventType, T data) {}