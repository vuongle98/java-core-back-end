package com.vuog.core.module.notification.domain.event;

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
public class NotificationEvent implements StreamMessage {
    private String type; // EMAIL, SMS, PUSH, SYSTEM
    private String recipient;
    private String subject;
    private String content;
    private String sender;
    private long timestamp;

    @Override
    public String topic() {
        return "notification." + type.toLowerCase();
    }

    @Override
    public String toPayload() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize notification event", e);
        }
    }
}
