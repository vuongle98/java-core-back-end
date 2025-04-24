package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends
        BaseRepository<Token>, BaseQueryRepository<Token> {

    Optional<Token> findByTokenAndIsBlacklistedFalse(String token);

    List<Token> findAllByTokenAndIsBlacklistedFalse(String token);

    List<Token> findAllRelatedByTokenAndIsBlacklistedFalseOrderByExpireAtDesc(String token);

    Optional<Token> findFirstByTokenAndIsBlacklistedFalseOrderByExpireAtDesc(String token);

    Optional<Token> findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeAndTokenOrderByExpireAtDesc(User user, Instant expiredAt, Token.TokenType type, String token);

    Optional<Token> findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterAndTypeOrderByExpireAtDesc(User user, Instant expireAt, Token.TokenType type);

    boolean existsByTokenAndIsBlacklisted(String token, boolean isBlacklisted);

    List<Token> findAllByUserAndIsBlacklistedFalse(User user);
}
