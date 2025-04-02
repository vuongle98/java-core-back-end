package com.vuog.core.module.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserSettingReq implements Serializable {

    private String key;
    private String value;

}
