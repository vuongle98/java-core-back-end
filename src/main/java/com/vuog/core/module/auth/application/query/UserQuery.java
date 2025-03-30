package com.vuog.core.module.auth.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery implements Serializable {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String permission;
}