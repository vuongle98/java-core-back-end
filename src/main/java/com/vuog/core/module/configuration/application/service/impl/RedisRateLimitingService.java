package com.vuog.core.module.configuration.application.service.impl;

import com.vuog.core.module.configuration.application.service.RateLimitingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisRateLimitingService implements RateLimitingService {

    @Value("${app.rate-limiting}")
    private Long REQUEST_LIMIT = 10L;
    private static final Long TIME_WINDOW = 60L;

    private final RedisTemplate<String, Integer> redisTemplate;

    public RedisRateLimitingService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(Long userId, String requestIp) {
        String key;
        if (userId == null) {
            key = "rate_limit:ip:" + requestIp; // Nếu không có userId, chỉ limit theo IP
        } else {
            key = "rate_limit:ip-user:" + requestIp + ":" + userId; // Nếu có userId, dùng cả IP + userId
        }
        Integer requestCount = redisTemplate.opsForValue().get(key);

        if (requestCount == null) {
            redisTemplate.opsForValue().set(key, 1, TIME_WINDOW, TimeUnit.SECONDS);
            return true;
        } else if (requestCount < REQUEST_LIMIT) {
            redisTemplate.opsForValue().increment(key);
            return true;
        } else {
            return false;
        }
    }
}
