package com.vuog.core.module.auth.domain.service.impl;

import com.vuog.core.module.auth.domain.service.RoleDomainService;
import com.vuog.core.module.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class RoleDomainServiceImpl implements RoleDomainService {

    @Override
    public boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }
}
