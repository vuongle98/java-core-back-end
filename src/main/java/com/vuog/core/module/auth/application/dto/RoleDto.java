package com.vuog.core.module.auth.application.dto;

import com.vuog.core.module.auth.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String description;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.code = role.getCode();
        this.name = role.getName();
        this.description = role.getDescription();
    }
}
