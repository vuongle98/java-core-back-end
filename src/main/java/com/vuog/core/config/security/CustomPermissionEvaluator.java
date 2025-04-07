package com.vuog.core.config.security;

import com.vuog.core.module.auth.domain.model.User;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

@Transactional
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final RoleHierarchy roleHierarchy;

    public CustomPermissionEvaluator(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        if (authentication == null || permission == null) {
            return false;
        }

        // 🔹 Get all authorities, including inherited ones
        Collection<? extends GrantedAuthority> inheritedAuthorities =
                roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

        return inheritedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(permission.toString()));
    }

//    @Override
//    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//
//        if (authentication == null || permission == null) {
//            return false;
//        }
//
//        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
//            if ("*".equals(grantedAuthority.getAuthority())) {
//                return true;
//            }
//        }
//
//        return authentication
//                .getAuthorities()
//                .stream()
//                .anyMatch(g -> g.getAuthority().equals(permission.toString()));
//    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
