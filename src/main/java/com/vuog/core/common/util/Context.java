package com.vuog.core.common.util;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.exception.UserNotFoundException;
import com.vuog.core.module.auth.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

public class Context {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getUser() {
        // Check if the current user is set in the ThreadLocal (for seeding)
        User user = currentUser.get();

        if (user != null) {
            return user;
        }

        // Fall back to SecurityContextHolder if no thread-local user is set
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            throw new UserNotFoundException("User not authenticated");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new UserNotFoundException("User not authenticated");
        }

        return (User) authentication.getPrincipal();
    }

    // Set the current user for seeding or other special operations
    public static void setUser(User user) {
        currentUser.set(user);
    }

    public static void setSystemUser() {
        User systemUser = new User("system", "system@example.com");
        Context.setUser(systemUser);  // Set the default user for audit logs
    }

    // Clear the user after seeding or operation
    public static void clear() {
        currentUser.remove();
        SecurityContextHolder.clearContext();
    }

    public static String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return jwt.getSubject(); // sub is Keycloak userId
    }

    public static String getCurrentUsername() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return jwt.getClaimAsString("preferred_username");
    }

    public static boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
