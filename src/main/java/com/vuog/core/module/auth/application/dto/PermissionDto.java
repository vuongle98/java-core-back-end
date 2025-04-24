package com.vuog.core.module.auth.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.auth.domain.model.Permission;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto extends BaseDto implements Serializable {

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
