package com.vuog.core.module.auth.domain.service;

import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.user.domain.model.User;

public interface PermissionDomainService {

    boolean hasPermission(User user, Permission permission);
}
