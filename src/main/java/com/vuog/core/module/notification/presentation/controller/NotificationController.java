package com.vuog.core.module.notification.presentation.controller;

import com.vuog.core.module.notification.application.service.NotificationService;
import com.vuog.core.module.stream.application.service.LoggingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final LoggingService loggingService;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        // Log the API request
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("recipient", request.getRecipient());
        metadata.put("subject", request.getSubject());
        loggingService.info("Email notification request received", "api", metadata);

        // Send email notification (this will use StreamPublisher internally)
        notificationService.sendEmail(request.getRecipient(), request.getSubject(), request.getContent());

        return ResponseEntity.ok("Email notification sent");
    }

    @PostMapping("/system")
    public ResponseEntity<String> sendSystemNotification(@RequestBody SystemNotificationRequest request) {
        // Log the API request
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("recipient", request.getRecipient());
        loggingService.info("System notification request received", "api", metadata);

        // Send system notification (this will use StreamPublisher internally)
        notificationService.sendSystemNotification(request.getRecipient(), request.getContent());

        return ResponseEntity.ok("System notification sent");
    }

    @Data
    public static class EmailRequest {
        private String recipient;
        private String subject;
        private String content;
    }

    @Data
    public static class SystemNotificationRequest {
        private String recipient;
        private String content;
    }
}
