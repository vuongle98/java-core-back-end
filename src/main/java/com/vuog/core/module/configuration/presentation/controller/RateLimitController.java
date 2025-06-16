package com.vuog.core.module.configuration.presentation.controller;

import com.vuog.core.module.configuration.application.service.RateLimitingService;
import com.vuog.core.module.stream.application.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller to show current rate limit information
 */
@RestController
@RequestMapping("/api/rate-limit")
@RequiredArgsConstructor
public class RateLimitController {

    private final RateLimitingService rateLimitingService;
    private final LoggingService loggingService;

    /**
     * Get current rate limit information for the user
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getRateLimitInfo(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userIp = request.getRemoteAddr();
        Long userId = null;
        String username = "anonymous";

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            username = auth.getName();
            // In a real app, you would get the user ID here
            // For example: userId = userService.getUserIdByUsername(username);
        }

        int requestCount = rateLimitingService.getRequestCount(userId, userIp);
        int limit = rateLimitingService.getLimit(userId);
        long remainingTime = rateLimitingService.getRemainingTimeWindow(userId, userIp);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("ipAddress", userIp);
        response.put("currentCount", requestCount);
        response.put("limit", limit);
        response.put("remaining", Math.max(0, limit - requestCount));
        response.put("resetInSeconds", remainingTime);

        // Log the rate limit info request
        Map<String, Object> metadata = new HashMap<>(response);
        loggingService.info("Rate limit info requested for user: " + username, "api", metadata);

        return ResponseEntity.ok(response);
    }
}
