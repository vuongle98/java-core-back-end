package com.vuog.core.module.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordCommand implements Serializable {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
