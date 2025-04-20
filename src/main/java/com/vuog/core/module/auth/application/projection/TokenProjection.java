package com.vuog.core.module.auth.application.projection;

import com.vuog.core.module.auth.domain.model.Token;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;

import java.time.Instant;

@ProjectionDefinition(name = "tokenProjection", types = {Token.class})
public interface TokenProjection {

    Long getId();
    UserProjection getUser();
    String getToken();
    Token.TokenType getType();
    Instant getExpireAt();
    Instant getIssuedAt();
    Boolean isBlacklisted();
}
