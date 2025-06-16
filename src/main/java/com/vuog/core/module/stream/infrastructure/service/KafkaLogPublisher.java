package com.vuog.core.module.stream.infrastructure.service;

import com.vuog.core.module.stream.domain.event.LogEvent;
import com.vuog.core.module.stream.domain.event.LogEvent.LogLevel;
import com.vuog.core.module.stream.domain.service.EventPublisher;
import com.vuog.core.module.stream.domain.service.LogPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka implementation of LogPublisher
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaLogPublisher implements LogPublisher {

    private final EventPublisher eventPublisher;

    @Override
    public void log(LogLevel level, String message, String category, Map<String, Object> metadata) {
        LogEvent event = LogEvent.builder()
                .source("system")
                .level(level)
                .message(message)
                .category(category)
                .metadata(metadata != null ? metadata : new HashMap<>())
                .build();

        eventPublisher.publish(event);

        // Also log using slf4j
        switch (level) {
            case TRACE -> log.trace(message);
            case DEBUG -> log.debug(message);
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
            case ERROR -> log.error(message);
        }
    }

    @Override
    public void info(String message, String category, Map<String, Object> metadata) {
        log(LogLevel.INFO, message, category, metadata);
    }

    @Override
    public void error(String message, String category, Map<String, Object> metadata) {
        log(LogLevel.ERROR, message, category, metadata);
    }
}
