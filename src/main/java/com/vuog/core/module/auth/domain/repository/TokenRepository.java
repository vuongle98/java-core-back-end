package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends
        BaseRepository<Token>, BaseQueryRepository<Token> {

    Optional<Token> findByToken(String token);

    Optional<Token> findFirstByUserAndIsBlacklistedFalseAndExpireAtAfterOrderByExpireAtDesc(User user, LocalDateTime expireAt);

    boolean existsByTokenAndIsBlacklisted(String token, boolean isBlacklisted);

}
