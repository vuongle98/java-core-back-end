package com.vuog.core.module.configuration.application.service.impl;

import com.vuog.core.module.configuration.application.service.RateLimitingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisRateLimitingService implements RateLimitingService {

    private static final Long TIME_WINDOW = 60L; // seconds
    private final RedisTemplate<String, Integer> redisTemplate;
    @Value("${app.rate-limiting}")
    private Long REQUEST_LIMIT = 10L;
    @Value("${app.rate-limiting.premium-users:20}")
    private Long PREMIUM_REQUEST_LIMIT = 20L;

    public RedisRateLimitingService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(Long userId, String requestIp) {
        String key = getRateLimitKey(userId, requestIp);
        Integer requestCount = redisTemplate.opsForValue().get(key);
        Long limit = getEffectiveLimit(userId);

        if (requestCount == null) {
            redisTemplate.opsForValue().set(key, 1, TIME_WINDOW, TimeUnit.SECONDS);
            return true;
        } else if (requestCount < limit) {
            redisTemplate.opsForValue().increment(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getRequestCount(Long userId, String requestIp) {
        String key = getRateLimitKey(userId, requestIp);
        Integer count = redisTemplate.opsForValue().get(key);
        return count != null ? count : 0;
    }

    @Override
    public int getLimit(Long userId) {
        return getEffectiveLimit(userId).intValue();
    }

    @Override
    public long getRemainingTimeWindow(Long userId, String requestIp) {
        String key = getRateLimitKey(userId, requestIp);
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null && ttl > 0 ? ttl : TIME_WINDOW;
    }

    /**
     * Generate the Redis key for rate limiting
     */
    private String getRateLimitKey(Long userId, String requestIp) {
        if (userId == null) {
            return "rate_limit:ip:" + requestIp; // If no userId, only limit by IP
        } else {
            return "rate_limit:ip-user:" + requestIp + ":" + userId; // If userId exists, use both IP and userId
        }
    }

    /**
     * Get the effective rate limit based on user type
     * Could be extended to read from user profile for different user tiers
     */
    private Long getEffectiveLimit(Long userId) {
        // Example: Premium users could have higher rate limits
        // This could be enhanced to read from user profile or configuration
        if (userId != null) {
            // Check if user is premium or has special limits
            // For now, use a simple premium user limit from application properties
            return PREMIUM_REQUEST_LIMIT;
        }
        return REQUEST_LIMIT;
    }
}