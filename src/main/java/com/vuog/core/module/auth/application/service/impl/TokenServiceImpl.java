package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.application.service.TokenService;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<Token> findByUserAndType(User user, Token.TokenType tokenType) {
        return tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeOrderByExpireAtDesc(user, Instant.now(), tokenType);
    }

    /*
    * Create token
    */
    public Token create(
            String tokenValue,
            Token.TokenType type,
            Instant expireTime,
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

@Transactional
public void blacklistTokenAndRelated(String tokenStr) {
    Instant now = Instant.now();

    // Find main tokens
    List<Token> tokens = tokenRepository.findAllByTokenAndIsBlacklistedFalse(tokenStr);

    // Collect all related tokens, including the main ones
    List<Token> allTokensToBlacklist = new ArrayList<>(tokens);

    for (Token token : tokens) {
        List<Token> relatedTokens = tokenRepository.findAllRelatedByTokenAndIsBlacklistedFalseOrderByExpireAtDesc(token.getToken());
        allTokensToBlacklist.addAll(relatedTokens);
    }

    // Set status and expiration
    for (Token token : allTokensToBlacklist) {
        token.setIsBlacklisted(true);
        token.setExpireAt(now);
    }

    // Batch save for efficiency
    tokenRepository.saveAll(allTokensToBlacklist);
}

    @Override
    public void blacklistToken(String token) {
        Token storedToken = tokenRepository.findByTokenAndIsBlacklistedFalse(token).orElseThrow(() -> new IllegalArgumentException("Token not found"));

        storedToken.setIsBlacklisted(true);
        storedToken.setExpireAt(Instant.now());

        tokenRepository.save(storedToken);
    }

    @Override
    public List<Token> findAllByUser(User user) {
        return tokenRepository.findAllByUserAndIsBlacklistedFalse(user);
    }

    @Override
    public Optional<Token> findValidByTokenAndUser(String token, User user) {
        return tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeAndTokenOrderByExpireAtDesc(user, Instant.now(), Token.TokenType.REFRESH, token);
    }

    @Override
    public boolean checkTokenType(String token, Token.TokenType tokenType) {
//        Optional<Token> tokenOptional = tokenRepository.findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeAndTokenOrderByExpireAtDesc()
        return true;
    }

    @Override
    public Optional<Token> findValidByToken(String token) {
        return tokenRepository.findFirstByTokenAndIsBlacklistedFalseOrderByExpireAtDesc(token);
    }

}