package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.domain.repository.EndpointSecureRepository;
import com.vuog.core.module.auth.application.service.EndpointSecurityService;
import com.vuog.core.module.auth.domain.model.EndpointSecure;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EndpointSecurityServiceImpl implements EndpointSecurityService {


    private final EndpointSecureRepository endpointSecureRepository;

    public EndpointSecurityServiceImpl(EndpointSecureRepository endpointSecureRepository) {
        this.endpointSecureRepository = endpointSecureRepository;
    }


    @Override
    public void applyDynamicRules(HttpSecurity http) throws Exception {
        List<EndpointSecure> rules = endpointSecureRepository.findAll();

        http.authorizeHttpRequests(auth -> {
            for (EndpointSecure rule : rules) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(rule.getEndpointPattern(), rule.getMethod());

                if (rule.getIsRole()) {
                    auth.requestMatchers(matcher).hasRole(rule.getAuthority());
                } else {
                    auth.requestMatchers(matcher).hasAuthority(rule.getAuthority());
                }
            }
        });
    }
}
