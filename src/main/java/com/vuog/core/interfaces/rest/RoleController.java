package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.common.util.ModelMappingUtil;
import com.vuog.core.module.auth.application.command.UpdateRoleReq;
import com.vuog.core.module.auth.application.dto.RoleDto;
import com.vuog.core.module.auth.application.service.RoleService;
import com.vuog.core.module.auth.domain.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleReq updateRoleReq
    ) {
        Role role = roleService.update(id, updateRoleReq);

        RoleDto roleDto = ModelMappingUtil.map(role, RoleDto.class);
        return ResponseEntity.ok(ApiResponse.success(roleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> deleteRole(
            @PathVariable Long id
    ) {
        roleService.delete(id);
        return ResponseEntity.ok().build();
    }



}
