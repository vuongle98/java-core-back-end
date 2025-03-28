package com.vuog.core.module.auth.domain.service.impl;

import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.auth.domain.service.PermissionDomainService;
import com.vuog.core.module.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class PermissionDomainServiceImpl implements PermissionDomainService {


    @Override
    public boolean hasPermission(User user, Permission permission) {
        return false;
    }
}
