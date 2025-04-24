package com.vuog.core.module.auth.application.dto;

import com.vuog.core.module.auth.domain.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String description;

    public PermissionDto(Permission permission) {
        this.id = permission.getId();
        this.code = permission.getCode();
        this.name = permission.getName();
        this.description = permission.getDescription();
    }
}
