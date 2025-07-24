package com.vuog.core.module.auth.application.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vuog.core.module.auth.domain.repository.EndpointSecureRepository;
import com.vuog.core.module.auth.application.service.EndpointSecurityService;
import com.vuog.core.module.auth.domain.model.EndpointSecure;
import com.vuog.core.common.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EndpointSecurityServiceImpl implements EndpointSecurityService {

    private static final String FIND_ALL = "endpoint_secure_rules";

    private final EndpointSecureRepository endpointSecureRepository;
    private final RedisCacheService redisCacheService;

    @Override
    public List<EndpointSecure> findAll() {
        return redisCacheService.getOrLoad(
                FIND_ALL,
                endpointSecureRepository::findAll,
                Duration.ofMinutes(10),
                new TypeReference<>() {}
        );
    }

    @Override
    public Optional<EndpointSecure> findByEndpoint(String endpointPattern, String method) {
        String key = String.format("FIND_BY_ENDPOINT:%s:%s", endpointPattern, method);

        return Optional.ofNullable(redisCacheService.getOrLoad(
                key,
                () -> endpointSecureRepository.findByEndpointPatternAndMethod(endpointPattern, method).orElse(null),
                Duration.ofMinutes(10),
                new TypeReference<>() {}
        ));
    }

    @Override
    public void applyDynamicRules(HttpSecurity http) throws Exception {
        List<EndpointSecure> rules = endpointSecureRepository.findAll();

        http.authorizeHttpRequests(auth -> {
            for (EndpointSecure rule : rules) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(rule.getEndpointPattern(), rule.getMethod());

                String authority = rule.getAuthority();

                if (rule.getIsRole()) {
                    // In Keycloak, roles are prefixed with "ROLE_" by the Jwt converter
                    auth.requestMatchers(matcher).hasRole(authority);
                } else {
                    // For custom authorities
                    auth.requestMatchers(matcher).hasAuthority(authority);
                }
            }
        });
    }

    @Override
    public EndpointSecure save(EndpointSecure endpointSecure) {
        return endpointSecureRepository.save(endpointSecure);
    }
}
