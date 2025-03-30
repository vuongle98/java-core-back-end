package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.EndpointSecure;

import java.util.Optional;

public interface EndpointSecureRepository extends
        BaseRepository<EndpointSecure>,
        BaseQueryRepository<EndpointSecure> {

    Optional<EndpointSecure> findByEndpointPatternAndMethod(String endpointPattern, String method);
}
