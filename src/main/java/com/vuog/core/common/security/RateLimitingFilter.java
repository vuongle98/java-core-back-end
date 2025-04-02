package com.vuog.core.common.security;

import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import com.vuog.core.module.configuration.application.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;
    private final UserRepository userRepository;

    public RateLimitingFilter(RateLimitingService rateLimitingService, UserRepository userRepository) {
        this.rateLimitingService = rateLimitingService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userIp = request.getRemoteAddr();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user != null && !rateLimitingService.isAllowed(user.getId(), userIp)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Rate limit reached. Please try again later.");
                return;
            }
        } else if (!rateLimitingService.isAllowed(null, userIp)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Rate limit reached. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
