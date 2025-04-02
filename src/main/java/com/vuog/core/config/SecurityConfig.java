package com.vuog.core.config;

import com.vuog.core.common.security.CustomAuthorizationManager;
import com.vuog.core.common.security.CustomPermissionEvaluator;
import com.vuog.core.common.security.RateLimitingFilter;
import com.vuog.core.common.security.jwt.AuthEntryPointJwt;
import com.vuog.core.common.security.jwt.AuthTokenFilter;
import com.vuog.core.module.auth.application.service.DynamicRoleHierarchyService;
import com.vuog.core.module.auth.application.service.EndpointSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final DynamicRoleHierarchyService dynamicRoleHierarchyService;
    private final EndpointSecurityService endpointSecurityService;
    private final CustomAuthorizationManager customAuthorizationManager;
    private final RateLimitingFilter rateLimitingFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        endpointSecurityService.applyDynamicRules(http);
        http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/auth/**"),
                                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                                AntPathRequestMatcher.antMatcher("/actuator/**"),
                                AntPathRequestMatcher.antMatcher("/ws/**"),
                                AntPathRequestMatcher.antMatcher("/error"),
                                AntPathRequestMatcher.antMatcher("/favicon.ico")
                        ).permitAll()
                        .anyRequest().access(customAuthorizationManager)
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .httpBasic(Customizer.withDefaults());

        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(roleHierarchy()));
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        return dynamicRoleHierarchyService.getRoleHierarchy();
    }
}
