package com.vuog.core.module.auth.domain.service.impl;

import com.vuog.core.common.exception.AccessDeniedException;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import com.vuog.core.module.auth.domain.service.RoleDomainService;
import com.vuog.core.module.auth.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class RoleDomainServiceImpl implements RoleDomainService {

    private final RoleRepository roleRepository;

    public RoleDomainServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }

    public void validateCanModifyRole(Role role, User currentUser) {
        if (role.isProtected()) {
            throw new AccessDeniedException("Cannot modify the Super Admin role.");
        }

        if (!currentUser.isSuperAdmin()) {
            throw new AccessDeniedException("Only Super Admin can modify roles.");
        }
    }

    public void validateCanRemoveInheritedRole(Role roleToRemove, User currentUser) {
        if (!currentUser.isSuperAdmin()) {
            throw new AccessDeniedException("Only Super Admin can remove inherited roles.");
        }

        if (roleToRemove.isProtected() || roleToRemove.isSuperAdminRole()) {
            throw new AccessDeniedException("Cannot modify the Super Admin role.");
        }

        boolean isParentRole = roleRepository.existsByChildRolesContaining(roleToRemove);
        if (isParentRole) {
            throw new IllegalStateException("Cannot remove a role that is a parent of another role.");
        }

        if (!currentUser.isSuperAdmin()) {
            throw new AccessDeniedException("Only SUPER_ADMIN can remove roles.");
        }
    }
}
