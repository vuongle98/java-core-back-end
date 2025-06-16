package com.vuog.core.config.security.event;

import com.vuog.core.common.event.StreamMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitEvent implements StreamMessage {
    private String type; // EXCEEDED, WARNING
    private Long userId;
    private String username;
    private String ipAddress;
    private int requestCount;
    private int limit;
    private long timestamp;

    @Override
    public String topic() {
        return "security.rate-limit";
    }

    @Override
    public String toPayload() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize rate limit event", e);
        }
    }
}
