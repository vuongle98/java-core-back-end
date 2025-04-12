package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.common.util.Context;
import com.vuog.core.module.auth.application.command.UpdateRoleReq;
import com.vuog.core.module.auth.application.service.RoleService;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import com.vuog.core.module.auth.domain.service.RoleDomainService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleDomainService roleDomainService;

    public RoleServiceImpl(RoleRepository roleRepository, RoleDomainService roleDomainService) {
        this.roleRepository = roleRepository;
        this.roleDomainService = roleDomainService;
    }


    @Override
    public Role update(Long id, UpdateRoleReq updateRoleReq) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        roleDomainService.validateCanModifyRole(role, Context.getUser());

        if (Objects.nonNull(updateRoleReq.getName())) {
            role.setName(updateRoleReq.getName());
        }

        if (Objects.nonNull(updateRoleReq.getDescription())) {
            role.setDescription(updateRoleReq.getDescription());
        }

        if (Objects.nonNull(updateRoleReq.getCode())) {
            role.setCode(updateRoleReq.getCode());
        }

        return roleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        roleDomainService.validateCanRemoveInheritedRole(role, Context.getUser());

        roleRepository.delete(role);
    }


}
