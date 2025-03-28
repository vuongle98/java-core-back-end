package com.vuog.core.module.user.application.command;

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

    private long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Set<Long> roleIds;
    private String status;
    private String firstName;
    private String lastName;
}
