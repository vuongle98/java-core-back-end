package com.vuog.core.common.util;

import com.vuog.core.common.exception.UserNotFoundException;
import com.vuog.core.module.auth.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class Context {

    public static User getUser() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            throw new UserNotFoundException("User not authenticated");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new UserNotFoundException("User not authenticated");
        }

        return (User) authentication.getPrincipal();
    }
}
