package com.vuog.core.config;

import com.vuog.core.module.configuration.application.service.RateLimitingService;
import com.vuog.core.module.configuration.application.service.impl.RedisRateLimitingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RateLimitingConfig {

    private final RedisRateLimitingService redisRateLimitingService;

    // db service or others

    public RateLimitingConfig(RedisRateLimitingService redisRateLimitingService) {
        this.redisRateLimitingService = redisRateLimitingService;
    }

    @Bean
    @Primary
    public RateLimitingService rateLimitingService() {
        return redisRateLimitingService;
    }

}
