package com.vuog.core.module.auth.application.service;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

public interface DynamicRoleHierarchyService {

    RoleHierarchy getRoleHierarchy();
}
