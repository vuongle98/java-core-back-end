package com.vuog.core.module.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileCommand implements Serializable {

    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl;
    private String address;
}
