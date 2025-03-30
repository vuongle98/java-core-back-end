package com.vuog.core.common.util;

import com.vuog.core.module.auth.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class Context {

    public static User getUser() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }
}
