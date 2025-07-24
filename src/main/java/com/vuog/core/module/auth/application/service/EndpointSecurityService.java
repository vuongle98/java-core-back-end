package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.domain.model.EndpointSecure;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.List;
import java.util.Optional;

public interface EndpointSecurityService {

    List<EndpointSecure> findAll();

    Optional<EndpointSecure> findByEndpoint(String endpointPattern, String method);
    void applyDynamicRules(HttpSecurity http) throws Exception;

    EndpointSecure save(EndpointSecure endpointSecure);
}
