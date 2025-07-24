package com.vuog.core.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Get value from Redis cache or fallback to DB/service.
     *
     * @param key           Cache key
     * @param supplier      Fallback function to get fresh data
     * @param ttl           Time to live (can be null)
     * @param <T>           Value type
     * @return              Cached or fresh value
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrLoad(String key, Supplier<T> supplier, Duration ttl) {
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return (T) cached;
        }

        T value = supplier.get();
        if (ttl != null) {
            redisTemplate.opsForValue().set(key, value, ttl);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
        return value;
    }


    public <T> T getOrLoad(String key, Supplier<T> loader, Duration ttl, TypeReference<T> typeRef) {
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return objectMapper.convertValue(cached, typeRef);
        }

        T value = loader.get();
        redisTemplate.opsForValue().set(key, value, ttl);
        return value;
    }

    /**
     * Put value into cache manually
     */
    public <T> void put(String key, T value, Duration ttl) {
        if (ttl != null) {
            redisTemplate.opsForValue().set(key, value, ttl);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * Get value if exists
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        Object cached = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable((T) cached);
    }

    /**
     * Delete key from cache
     */
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Force reload by replacing value
     */
    public <T> T reload(String key, Supplier<T> supplier, Duration ttl) {
        T value = supplier.get();
        put(key, value, ttl);
        return value;
    }

    /**
     * Remove all entity's key set in Redis.
     */
    public void removeAllKeysFromRedis() {
        redisTemplate.keys("*").forEach(redisTemplate::delete);
    }

    /**
     * Add cache key to the entity's key set in Redis.
     */
    public void addCacheKeyToEntity(String entityKey, String cacheKey) {
        stringRedisTemplate.opsForSet().add(entityKey, cacheKey);
    }

    /**
     * Get all cache keys related to an entity.
     */
    public Set<String> getCacheKeysForEntity(String entityKey) {
        return stringRedisTemplate.opsForSet().members(entityKey);
    }

    /**
     * Remove a cache key from the entity's key set in Redis.
     */
    public void removeCacheKeyFromEntity(String entityKey, String cacheKey) {
        stringRedisTemplate.opsForSet().remove(entityKey, cacheKey);
    }

    /**
     * Remove all cache keys related to an entity.
     */
    public void removeAllCacheKeysForEntity(String entityKey) {
        Set<String> cacheKeys = stringRedisTemplate.opsForSet().members(entityKey);
        if (cacheKeys != null) {
            stringRedisTemplate.opsForSet().remove(entityKey, cacheKeys.toArray());
        }
    }

    /**
     * Remove an entity's key set in Redis.
     */
    public void removeEntityKeyFromRedis(String entityKey) {
        stringRedisTemplate.delete(entityKey);
    }
}
