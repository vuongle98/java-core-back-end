package com.vuog.core.module.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleReq {

    private String code;
    private String name;
    private String description;
    private List<Long> permissionIds;
}
