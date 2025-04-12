package com.vuog.core.config.security;


import com.vuog.core.common.dto.UserRequestLog;
import com.vuog.core.common.event.UserRequestLogEvent;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.net.InetAddress;

@Component
public class UserRequestLogFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserRequestLogFilter(UserRepository userRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElseGet(() -> new User(username));
            userRequestLog.setUser(new UserDto(user));
        } else {
            userRequestLog.setUser(new UserDto("ANONYMOUS"));
        }

        applicationEventPublisher.publishEvent(new UserRequestLogEvent(userRequestLog));
    }
}
