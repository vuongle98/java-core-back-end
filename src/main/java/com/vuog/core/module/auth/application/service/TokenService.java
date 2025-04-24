package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenService {

    Optional<Token> findByUserAndType(User user, Token.TokenType tokenType);

    Token create(String tokenValue, Token.TokenType tokenType, Instant expirationTime, User user, Token relatedToken);

    boolean isTokenBlacklisted(String token);

    void blacklistTokenAndRelated(String token);

    void blacklistToken(String token);

    List<Token> findAllByUser(User user);

    Optional<Token> findValidByTokenAndUser(String token, User user);

    Optional<Token> findValidByToken(String token);

    boolean checkTokenType(String token, Token.TokenType tokenType);
}
