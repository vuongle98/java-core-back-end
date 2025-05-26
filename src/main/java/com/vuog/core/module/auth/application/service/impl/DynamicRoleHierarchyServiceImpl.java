package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.application.service.DynamicRoleHierarchyService;
import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DynamicRoleHierarchyServiceImpl implements DynamicRoleHierarchyService {

    @Value("${app.master-admin-role}")
    private String MASTER_ADMIN_ROLE;

    private final RoleRepository roleRepository;

    public DynamicRoleHierarchyServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleHierarchy getRoleHierarchy() {
        List<Role> roles = roleRepository.findAll();
        Set<String> hierarchyLines = new HashSet<>();

        // Build hierarchy from parent-child relationships between roles
        for (Role role : roles) {
            String roleCode = role.getCode();

            if (!MASTER_ADMIN_ROLE.equals(role.getCode())) {
                // Add hierarchy between parent and child roles
                for (Role childRole : role.getChildRoles()) {
                    hierarchyLines.add(roleCode + " > " + childRole.getCode());
                }
                
                // Add hierarchy between role and its permissions
                for (Permission permission : role.getPermissions()) {
                    hierarchyLines.add(roleCode + " > " + permission.getCode());
                }

                hierarchyLines.add(MASTER_ADMIN_ROLE + " > " + roleCode);
            }
        }

        String hierarchyString = String.join("\n", hierarchyLines);
        return RoleHierarchyImpl.fromHierarchy(hierarchyString);
    }
}
