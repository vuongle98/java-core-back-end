package com.vuog.core.module.auth.application.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionQuery implements Serializable {

    private String code;
    private String name;
    private String description;
}
