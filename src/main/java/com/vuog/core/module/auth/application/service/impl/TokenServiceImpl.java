package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.application.service.TokenService;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<Token> findByUser(User user) {
        return tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterOrderByExpireAtDesc(user, LocalDateTime.now());
    }

    /*
    * Create token
    */
    public Token create(
            String tokenValue,
            Token.TokenType type,
            LocalDateTime expireTime,
            User user
    ) {

        Token token = new Token();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setType(type);
        token.setExpireAt(expireTime);
        token.setIsBlacklisted(false);

        return tokenRepository.save(token);
    }

    /**
     * Checks if a token is blacklisted.
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenRepository.existsByTokenAndIsBlacklisted(token, true);
    }

    public void blacklist(String token) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(token);
        tokenOpt.ifPresent(t -> {
            t.setIsBlacklisted(true);
            tokenRepository.save(t);
        });
    }

}
