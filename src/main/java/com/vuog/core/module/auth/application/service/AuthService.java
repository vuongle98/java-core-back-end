package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.domain.event.UserEvent;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import com.vuog.core.module.stream.application.event.StreamPublisher;
import com.vuog.core.module.stream.application.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StreamPublisher streamPublisher;
    private final LoggingService loggingService;

    /**
     * Handle user login
     */
    public void login(String username, String password, String ipAddress) {
        // Find user and validate password (implementation not shown)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create user login event with just the data we need
        // Avoid passing the entire entity to prevent serialization issues
        UserEvent loginEvent = UserEvent.builder()
                .eventType("LOGIN")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .ipAddress(ipAddress)
                .timestamp(System.currentTimeMillis())
                .build();

        // Publish event to Kafka with user ID as the key
        streamPublisher.publish(user.getId().toString(), loginEvent);

        // Also log the login
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", user.getId());
        metadata.put("ipAddress", ipAddress);
        loggingService.info("User logged in: " + username, "security", metadata);
    }

    /**
     * Handle user registration
     */
    public User register(User user, String ipAddress) {
        // Save user (implementation not shown)
        User savedUser = userRepository.save(user);

        // Create user registration event with just the data we need
        UserEvent registrationEvent = UserEvent.builder()
                .eventType("REGISTER")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .ipAddress(ipAddress)
                .timestamp(System.currentTimeMillis())
                .build();

        // Publish event to Kafka
        streamPublisher.publish(savedUser.getId().toString(), registrationEvent);

        // Log the registration
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", savedUser.getId());
        metadata.put("ipAddress", ipAddress);
        loggingService.info("New user registered: " + savedUser.getUsername(), "security", metadata);

        return savedUser;
    }

    /**
     * Handle password change
     */
    public void changePassword(Long userId, String newPassword, String ipAddress) {
        // Find user and update password (implementation not shown)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update password logic here

        // Create password change event with just the data we need
        UserEvent passwordChangeEvent = UserEvent.builder()
                .eventType("PASSWORD_CHANGE")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .ipAddress(ipAddress)
                .timestamp(System.currentTimeMillis())
                .build();

        // Publish event to Kafka
        streamPublisher.publish(user.getId().toString(), passwordChangeEvent);

        // Log the password change
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", user.getId());
        metadata.put("ipAddress", ipAddress);
        loggingService.info("Password changed for user: " + user.getUsername(), "security", metadata);
    }
}
