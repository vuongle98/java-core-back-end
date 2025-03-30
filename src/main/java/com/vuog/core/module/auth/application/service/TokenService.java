package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenService {

    Optional<Token> findByUser(User user);

    Token create(String tokenValue, Token.TokenType tokenType, LocalDateTime expirationTime, User user);

    boolean isTokenBlacklisted(String token);

    void blacklist(String token);
}
