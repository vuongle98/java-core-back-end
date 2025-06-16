package com.vuog.core.config.security;

import com.vuog.core.config.security.event.RateLimitEvent;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import com.vuog.core.module.configuration.application.service.RateLimitingService;
import com.vuog.core.module.stream.application.event.StreamPublisher;
import com.vuog.core.module.stream.application.service.LoggingService;
import jakarta.servlet.FilterChain;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;
    private final UserRepository userRepository;
    private final StreamPublisher streamPublisher;
    private final LoggingService loggingService;

    public RateLimitingFilter(RateLimitingService rateLimitingService, UserRepository userRepository, 
                              StreamPublisher streamPublisher, LoggingService loggingService) {
        this.rateLimitingService = rateLimitingService;
        this.userRepository = userRepository;
        this.streamPublisher = streamPublisher;
        this.loggingService = loggingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userIp = request.getRemoteAddr();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user != null && !rateLimitingService.isAllowed(user.getId(), userIp)) {
                // Create and publish rate limit exceeded event
                RateLimitEvent event = RateLimitEvent.builder()
                        .type("EXCEEDED")
                        .userId(user.getId())
                        .username(username)
                        .ipAddress(userIp)
                        .requestCount(rateLimitingService.getRequestCount(user.getId(), userIp))
                        .limit(rateLimitingService.getLimit(user.getId()))
                        .timestamp(System.currentTimeMillis())
                        .build();

                // Publish to Kafka
                streamPublisher.publish(user.getId().toString(), event);

                // Log rate limit exceeded
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("userId", user.getId());
                metadata.put("username", username);
                metadata.put("ipAddress", userIp);
                metadata.put("requestCount", rateLimitingService.getRequestCount(user.getId(), userIp));
                loggingService.error("Rate limit exceeded for user: " + username, "security", metadata);

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Rate limit reached. Please try again later.");
                return;
            }
        } else if (!rateLimitingService.isAllowed(null, userIp)) {
            // Create and publish anonymous rate limit exceeded event
            RateLimitEvent event = RateLimitEvent.builder()
                    .type("EXCEEDED")
                    .ipAddress(userIp)
                    .requestCount(rateLimitingService.getRequestCount(null, userIp))
                    .limit(rateLimitingService.getLimit(null))
                    .timestamp(System.currentTimeMillis())
                    .build();

            // Publish to Kafka
            streamPublisher.publish(userIp, event);

            // Log rate limit exceeded
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("ipAddress", userIp);
            metadata.put("requestCount", rateLimitingService.getRequestCount(null, userIp));
            loggingService.error("Rate limit exceeded for anonymous user", "security", metadata);

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Rate limit reached. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
