package com.vuog.core.module.notification.application.service;

import com.vuog.core.module.notification.domain.event.NotificationEvent;
import com.vuog.core.module.stream.application.event.StreamPublisher;
import com.vuog.core.module.stream.application.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StreamPublisher streamPublisher;
    private final LoggingService loggingService;

    /**
     * Send an email notification
     */
    public void sendEmail(String recipient, String subject, String content) {
        NotificationEvent event = NotificationEvent.builder()
                .type("EMAIL")
                .recipient(recipient)
                .subject(subject)
                .content(content)
                .sender("system")
                .timestamp(System.currentTimeMillis())
                .build();

        // Publish to Kafka using recipient as key for message ordering
        streamPublisher.publish(recipient, event);

        // Log the notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("recipient", recipient);
        metadata.put("subject", subject);
        loggingService.info("Email notification sent", "notification", metadata);
    }

    /**
     * Send a system notification
     */
    public void sendSystemNotification(String recipient, String content) {
        NotificationEvent event = NotificationEvent.builder()
                .type("SYSTEM")
                .recipient(recipient)
                .subject("System Notification")
                .content(content)
                .sender("system")
                .timestamp(System.currentTimeMillis())
                .build();

        // Publish to Kafka
        streamPublisher.publish(recipient, event);

        // Log the notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("recipient", recipient);
        metadata.put("content", content);
        loggingService.info("System notification sent", "notification", metadata);
    }
}
