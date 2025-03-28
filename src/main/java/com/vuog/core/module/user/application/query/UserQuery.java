package com.vuog.core.module.user.application.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserQuery implements Serializable {
    private String username;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String status;
}