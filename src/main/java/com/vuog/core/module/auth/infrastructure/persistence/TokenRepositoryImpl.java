package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.common.repository.OwnableRepository;
import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.auth.domain.repository.TokenRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepositoryImpl extends
        GenericRepository<Token, Long>, TokenRepository, OwnableRepository<Token, Long> {

    default Class<Token> getEntityClass() {
        return Token.class;
    }

    boolean existsByIdAndCreatedBy(Long id, String createdBy);
}
