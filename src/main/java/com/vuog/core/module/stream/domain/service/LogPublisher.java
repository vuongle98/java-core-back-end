package com.vuog.core.module.stream.domain.service;

import com.vuog.core.module.stream.domain.event.LogEvent.LogLevel;

import java.util.Map;

/**
 * Domain service interface for publishing structured logs
 */
public interface LogPublisher {

    /**
     * Publish a log message
     * @param level Log level
     * @param message Log message
     * @param category Log category
     * @param metadata Additional metadata
     */
    void log(LogLevel level, String message, String category, Map<String, Object> metadata);

    /**
     * Publish an info log message
     * @param message Log message
     * @param category Log category
     * @param metadata Additional metadata
     */
    void info(String message, String category, Map<String, Object> metadata);

    /**
     * Publish an error log message
     * @param message Log message
     * @param category Log category
     * @param metadata Additional metadata
     */
    void error(String message, String category, Map<String, Object> metadata);
}