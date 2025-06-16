package com.vuog.core.module.stream.application.service;

import com.vuog.core.module.stream.domain.event.LogEvent.LogLevel;
import com.vuog.core.module.stream.domain.service.LogPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Application service for structured logging
 */
@Service
@RequiredArgsConstructor
public class LoggingService {

    private final LogPublisher logPublisher;

    /**
     * Log an info message
     * @param message The log message
     * @param category The log category
     */
    public void info(String message, String category) {
        logPublisher.info(message, category, new HashMap<>());
    }

    /**
     * Log an info message with metadata
     * @param message The log message
     * @param category The log category
     * @param metadata Additional metadata
     */
    public void info(String message, String category, Map<String, Object> metadata) {
        logPublisher.info(message, category, metadata);
    }

    /**
     * Log an error message
     * @param message The log message
     * @param category The log category
     */
    public void error(String message, String category) {
        logPublisher.error(message, category, new HashMap<>());
    }

    /**
     * Log an error message with metadata
     * @param message The log message
     * @param category The log category
     * @param metadata Additional metadata
     */
    public void error(String message, String category, Map<String, Object> metadata) {
        logPublisher.error(message, category, metadata);
    }

    /**
     * Log a message with a specific log level
     * @param level The log level
     * @param message The log message
     * @param category The log category
     * @param metadata Additional metadata
     */
    public void log(LogLevel level, String message, String category, Map<String, Object> metadata) {
        logPublisher.log(level, message, category, metadata);
    }
}
