package com.vuog.core.module.user.application.dto;

import com.vuog.core.common.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {
    private String username;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String status;
}
