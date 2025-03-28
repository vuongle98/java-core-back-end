package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.application.service.RoleService;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
