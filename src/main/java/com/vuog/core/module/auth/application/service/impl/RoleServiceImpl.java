package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.common.util.Context;
import com.vuog.core.module.auth.application.command.UpdateRoleReq;
import com.vuog.core.module.auth.application.service.RoleService;
import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.repository.PermissionRepository;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import com.vuog.core.module.auth.domain.service.RoleDomainService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleDomainService roleDomainService;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleDomainService roleDomainService, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.roleDomainService = roleDomainService;
        this.permissionRepository = permissionRepository;
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

        if (Objects.nonNull(updateRoleReq.getPermissionIds())) {
            Set<Permission> newPerms = updateRoleReq.getPermissionIds().stream().map(permissionId -> permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new EntityNotFoundException("Permission not found"))).collect(java.util.stream.Collectors.toSet());
            role.setPermissions(newPerms);
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
