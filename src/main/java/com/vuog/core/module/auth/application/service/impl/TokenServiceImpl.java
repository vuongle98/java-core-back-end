package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.application.service.TokenService;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<Token> findByUserAndType(User user, Token.TokenType tokenType) {
        return tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeOrderByExpireAtDesc(user, LocalDateTime.now(), tokenType);
    }

    /*
    * Create token
    */
    public Token create(
            String tokenValue,
            Token.TokenType type,
            LocalDateTime expireTime,
            User user,
            Token relatedToken
    ) {

        Token token = new Token();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setType(type);
        token.setExpireAt(expireTime);
        token.setIsBlacklisted(false);
        token.setRelatedToken(token);

        return tokenRepository.save(token);
    }

    /**
     * Checks if a token is blacklisted.
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenRepository.existsByTokenAndIsBlacklisted(token, true);
    }

    public void blacklist(String tokenStr) {
        List<Token> tokens = tokenRepository.findAllByToken(tokenStr);

        for (Token token : tokens) {
            token.setIsBlacklisted(true);
            tokenRepository.save(token);
        }
    }

    @Override
    public List<Token> findAllByUser(User user) {
        return tokenRepository.findAllByUserAndIsBlacklistedFalse(user);
    }

    @Override
    public Optional<Token> findValidByTokenAndUser(String token, User user) {
        return tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeAndTokenOrderByExpireAtDesc(user, LocalDateTime.now(), Token.TokenType.REFRESH, token);
    }

    @Override
    public boolean checkTokenType(String token, Token.TokenType tokenType) {
//        Optional<Token> tokenOptional = tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeAndTokenOrderByExpireAtDesc()
        return true;
    }

}
