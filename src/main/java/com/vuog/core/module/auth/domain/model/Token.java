package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.user.domain.model.User;

import java.time.LocalDateTime;

public class Token extends BaseModel {

    private User user;

    private String token;

    private TokenType tokenType;

    private LocalDateTime expireAt;

    private LocalDateTime issuedAt;

    private enum TokenType {
        ACCESS, REFRESH
    }

}
