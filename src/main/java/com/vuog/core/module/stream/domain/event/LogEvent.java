package com.vuog.core.module.stream.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Event for structured logging to Kafka
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LogEvent extends BaseEvent {
    /**
     * Log level
     */
    private LogLevel level;

    /**
     * Log message
     */
    private String message;

    /**
     * Log category
     */
    private String category;

    /**
     * Additional structured data
     */
    private Map<String, Object> metadata;

    @Override
    public String getTopic() {
        return "log." + (category != null ? category.toLowerCase() : "general");
    }

    /**
     * Log level enumeration
     */
    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
