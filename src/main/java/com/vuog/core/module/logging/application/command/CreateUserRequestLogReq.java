package com.vuog.core.module.logging.application.command;

import com.vuog.core.module.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.InetAddress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestLogReq implements Serializable {

    private User user;
    private InetAddress ipAddress;
}
