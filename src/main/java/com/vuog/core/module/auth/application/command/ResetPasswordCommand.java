package com.vuog.core.module.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordCommand implements Serializable {

    private String email;
    private String username;
}
