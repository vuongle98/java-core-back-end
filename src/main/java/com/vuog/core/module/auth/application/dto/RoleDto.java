package com.vuog.core.module.auth.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.auth.domain.model.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends BaseDto implements Serializable {

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
