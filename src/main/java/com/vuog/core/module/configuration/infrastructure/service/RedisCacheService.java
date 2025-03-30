package com.vuog.core.module.configuration.infrastructure.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisCacheService {

    private final StringRedisTemplate redisTemplate;

    public RedisCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Add cache key to the entity's key set in Redis.
     */
    public void addCacheKeyToEntity(String entityKey, String cacheKey) {
        redisTemplate.opsForSet().add(entityKey, cacheKey);
    }

    /**
     * Get all cache keys related to an entity.
     */
    public Set<String> getCacheKeysForEntity(String entityKey) {
        return redisTemplate.opsForSet().members(entityKey);
    }

    /**
     * Remove a cache key from the entity's key set in Redis.
     */
    public void removeCacheKeyFromEntity(String entityKey, String cacheKey) {
        redisTemplate.opsForSet().remove(entityKey, cacheKey);
    }

    /**
     * Remove all cache keys related to an entity.
     */
    public void removeAllCacheKeysForEntity(String entityKey) {
        Set<String> cacheKeys = redisTemplate.opsForSet().members(entityKey);
        if (cacheKeys != null) {
            redisTemplate.opsForSet().remove(entityKey, cacheKeys.toArray());
        }
    }

    /**
     * Remove an entity's key set in Redis.
     */
    public void removeEntityKeyFromRedis(String entityKey) {
        redisTemplate.delete(entityKey);
    }
}
