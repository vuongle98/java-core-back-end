package com.vuog.core.module.auth.domain.service;

import com.vuog.core.module.auth.domain.model.User;

public interface RoleDomainService {

    boolean isAdmin(User user);
}
