package com.vuog.core.module.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserReq implements Serializable {

    private String username;
    private String email;
    private String password;
    private Set<Long> roleIds;
}
