package com.vuog.core.module.auth.application.service;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface EndpointSecurityService {

    void applyDynamicRules(HttpSecurity http) throws Exception;
}
