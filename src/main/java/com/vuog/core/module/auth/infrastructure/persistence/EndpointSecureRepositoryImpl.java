package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.module.auth.domain.repository.EndpointSecureRepository;
import com.vuog.core.module.auth.domain.model.EndpointSecure;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointSecureRepositoryImpl extends GenericRepository<EndpointSecure, Long>, EndpointSecureRepository {

    default Class<EndpointSecure> getEntityClass() {
        return EndpointSecure.class;
    }
}
