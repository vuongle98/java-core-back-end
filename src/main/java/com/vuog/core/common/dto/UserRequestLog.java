package com.vuog.core.common.dto;

import com.vuog.core.module.auth.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.InetAddress;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestLog implements Serializable {

    private String method;
    private String uri;
    private String queryString;
    private String payload;
    private InetAddress ipAddress;
    private UserDto user;
}
