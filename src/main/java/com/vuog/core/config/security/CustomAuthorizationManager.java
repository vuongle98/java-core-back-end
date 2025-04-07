package com.vuog.core.config.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        System.out.println("User: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities()); // Print user roles

        // Allow Master Admin (*) to access everything
        boolean isMasterAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("*"));

        if (isMasterAdmin) {
            return new AuthorizationDecision(true);
        }

        // For all other requests, let the method-level security handle it
        return new AuthorizationDecision(true);
    }
}
