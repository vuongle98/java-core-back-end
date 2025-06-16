package com.vuog.core.module.auth.domain.event;

import com.vuog.core.common.event.StreamMessage;
import com.vuog.core.common.util.JacksonUtils;
import com.vuog.core.module.auth.domain.model.User;
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
public class UserEvent implements StreamMessage {
    private String eventType; // LOGIN, REGISTER, PASSWORD_CHANGE, etc.
    private Long userId;
    private String username;
    private String email;
    private String ipAddress;
    private long timestamp;

    /**
     * Create a user event from a user object
     */
    public static UserEvent from(User user, String eventType, String ipAddress) {
        return UserEvent.builder()
                .eventType(eventType)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .ipAddress(ipAddress)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public String topic() {
        return "user." + eventType.toLowerCase();
    }

    @Override
    public String toPayload() {
        try {
            // Use a properly configured ObjectMapper from the spring context
            // This is a temporary solution; in production, inject the ObjectMapper
            return JacksonUtils.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize user event", e);
        }
    }
}
