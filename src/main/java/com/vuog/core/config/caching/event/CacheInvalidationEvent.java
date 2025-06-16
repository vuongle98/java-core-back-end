package com.vuog.core.config.caching.event;

import com.vuog.core.common.event.StreamMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheInvalidationEvent implements StreamMessage {
    private String entityKey;
    private List<String> cacheKeys;
    private String requestUri;
    private String requestMethod;
    private String userId;
    private String username;
    private long timestamp;

    @Override
    public String topic() {
        return "cache.invalidation";
    }

    @Override
    public String toPayload() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize cache invalidation event", e);
        }
    }
}
