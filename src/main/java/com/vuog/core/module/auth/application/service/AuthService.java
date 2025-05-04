package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.command.LoginCommand;
import com.vuog.core.module.auth.application.dto.JwtResponseDto;
import com.vuog.core.module.auth.domain.model.User;

public interface AuthService {

    JwtResponseDto getToken(LoginCommand command);

    JwtResponseDto refreshToken(String refreshToken);

    User register(CreateUserReq command);

    User verify();

    void logout();

    void revoke(String token);
}
