package com.vuog.core.module.auth.application.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleQuery implements Serializable {

    private String name;
    private String code;
    private String permissionId;
}
