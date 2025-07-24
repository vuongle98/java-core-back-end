package com.vuog.core.config.security;

import com.vuog.core.module.auth.application.service.EndpointSecurityService;
import com.vuog.core.module.auth.domain.model.EndpointSecure;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class DynamicRuleFilter extends OncePerRequestFilter {

    private final EndpointSecurityService endpointSecurityService;

    @Value("${app.master-admin-role}")
    private String ROLE_SUPER_ADMIN;

    public DynamicRuleFilter(EndpointSecurityService endpointSecurityService) {
        this.endpointSecurityService = endpointSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();            // e.g., GET, POST
        String url = request.getRequestURI();           // e.g., /api/user/123
        String entity = extractEntityFromUrl(url);      // --> user

        if (entity == null) {
            filterChain.doFilter(request, response);
            return;
        }

        List<EndpointSecure> rules = endpointSecurityService.findAll();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        for (EndpointSecure rule : rules) {
            String rulePattern = rule.getEndpointPattern(); // e.g., /api/{entity}/{id}
            String concretePattern = rulePattern.replace("{entity}", entity); // /api/user/{id}
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(concretePattern, rule.getMethod());

            if (!matcher.matches(request)) continue;

            String requiredAuthority = rule.getAuthority().replace("{ENTITY}", entity.toUpperCase());
            String fullRequired = rule.getIsRole() ? "ROLE_" + requiredAuthority : requiredAuthority;

            // Check if user has SUPER_ADMIN or the required authority
            if (auth == null || !auth.isAuthenticated() ||
                    auth.getAuthorities().stream().noneMatch(granted ->
                            granted.getAuthority().equals(ROLE_SUPER_ADMIN) || granted.getAuthority().equals(fullRequired))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                return;
            }

            break; // matched and authorized
        }

        filterChain.doFilter(request, response);
    }

    private String extractEntityFromUrl(String url) {
        // expected: /api/user, /api/category/123, /api/user/search
        String[] parts = url.split("/");
        if (parts.length >= 3 && parts[1].equals("api")) {
            return parts[2];
        }
        return null;
    }
}
