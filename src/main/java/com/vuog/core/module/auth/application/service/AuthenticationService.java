package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.stream.application.service.LoggingService;
import com.vuog.core.module.stream.domain.event.LogEvent.LogLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final LoggingService loggingService;

    public void login(String username, String password) {
        // Authentication logic here

        // Simple info log
        loggingService.info("User login attempt", "security");

        // Structured log with metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("username", username);
        metadata.put("ipAddress", "127.0.0.1");
        metadata.put("loginTime", System.currentTimeMillis());

        loggingService.info("User logged in successfully", "security", metadata);
    }

    public void loginFailed(String username, String reason) {
        // Log authentication failure
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("username", username);
        metadata.put("reason", reason);
        metadata.put("ipAddress", "127.0.0.1");

        loggingService.error("Login failed", "security", metadata);
    }

    public void registerUser(User user) {
        // Registration logic here

        // Use different log levels
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", user.getId());
        metadata.put("email", user.getEmail());

        loggingService.log(LogLevel.INFO, "New user registered", "user-management", metadata);
    }
}
