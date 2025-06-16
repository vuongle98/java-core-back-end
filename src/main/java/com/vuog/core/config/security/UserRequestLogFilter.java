package com.vuog.core.config.security;


import com.vuog.core.common.dto.UserRequestLog;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import com.vuog.core.module.stream.application.service.LoggingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRequestLogFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final LoggingService loggingService;

    public UserRequestLogFilter(UserRepository userRepository, LoggingService loggingService) {
        this.userRepository = userRepository;
        this.loggingService = loggingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userIp = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        UserRequestLog userRequestLog = new UserRequestLog();
        userRequestLog.setMethod(method);
        userRequestLog.setUri(uri);
        userRequestLog.setIpAddress(InetAddress.getByName(userIp));
        userRequestLog.setQueryString(request.getQueryString());

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);

        byte[] bodyBytes = wrappedRequest.getContentAsByteArray();
        String body = new String(bodyBytes, wrappedRequest.getCharacterEncoding());

        userRequestLog.setPayload(body);

        // Log the request using the stream module
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("method", method);
        metadata.put("uri", uri);
        metadata.put("ipAddress", userIp);
        metadata.put("queryString", request.getQueryString());
        metadata.put("responseStatus", response.getStatus());

        // Truncate body if too large
        if (body != null && body.length() > 1000) {
            metadata.put("payload", body.substring(0, 997) + "...");
        } else {
            metadata.put("payload", body);
        }

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElseGet(() -> new User(username));
            userRequestLog.setUser(new UserDto(user));
            metadata.put("username", username);

            // Log the request with user information
            loggingService.info("User request: " + method + " " + uri, "access-log", metadata);
        } else {
            userRequestLog.setUser(new UserDto("ANONYMOUS"));
            metadata.put("username", "ANONYMOUS");

            // Log anonymous request
            loggingService.info("Anonymous request: " + method + " " + uri, "access-log", metadata);
        }
    }
}
