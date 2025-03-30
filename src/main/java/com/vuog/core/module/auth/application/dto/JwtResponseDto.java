package com.vuog.core.module.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto implements Serializable {

    private String token;
    private String type;
    private UserDto user;
}
