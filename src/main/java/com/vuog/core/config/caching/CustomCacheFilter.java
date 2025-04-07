package com.vuog.core.config.caching;

import com.vuog.core.module.configuration.infrastructure.service.RedisCacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@WebFilter("/*")
@Component
public class CustomCacheFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomCacheFilter.class);
    private final CacheManager cacheManager;
    private final RedisCacheService redisCacheService;
    @Value("${app.cache-manager-name}")
    private String cacheManagerName;

    public CustomCacheFilter(CacheManager cacheManager, RedisCacheService redisCacheService) {
        this.cacheManager = cacheManager;
        this.redisCacheService = redisCacheService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/camunda")) {
            filterChain.doFilter(request, response);
            return;
        }

        String cacheKey = generateCacheKey(request);
        logger.debug("Processing request for URI: {}, Cache key: {}", request.getRequestURI(), cacheKey);

        Cache cache = cacheManager.getCache(cacheManagerName);

        if (!shouldCacheRequest(request)) {
            // Invalidate related cache keys for POST/PUT/PATCH/DELETE
            String entityKey = extractEntityKey(request);

            if (cache != null) {

                // Get all related cache keys for the entity from Redis
                Set<String> relatedCacheKeys = redisCacheService.getCacheKeysForEntity(entityKey);
                for (String key : relatedCacheKeys) {
                    cache.evict(key);
                    redisCacheService.removeCacheKeyFromEntity(entityKey, key);
                    logger.info("Invalidated related cache key: {}", key);
                }
            }

            filterChain.doFilter(request, response); // Proceed with the request
            return;
        }

        // Serve response from cache if available
        if (cache != null) {
            Cache.ValueWrapper cachedValue = cache.get(cacheKey);
            if (cachedValue != null) {
                serveCachedResponse(response, cachedValue, cacheKey);
                return;
            }
        }

        // Generate and cache the response
        CacheResponseWrapper responseWrapper = new CacheResponseWrapper(response);
        try {
            filterChain.doFilter(request, responseWrapper);

            if (response.getStatus() == HttpServletResponse.SC_OK) {
                String responseBody = responseWrapper.getResponseBody();
                if (cache != null && responseBody != null && !responseBody.isEmpty()) {
                    cache.put(cacheKey, responseBody);
                    logger.info("Response cached for key: {}", cacheKey);

                    String entityKey = extractEntityKey(request);
                    redisCacheService.addCacheKeyToEntity(entityKey, cacheKey);
                }
            }
        } catch (Exception e) {
            if (e.getCause().getClass().equals(UnsupportedOperationException.class)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Feature is not enabled");
            } else {
                logger.error("Error during caching operation for key: {}", cacheKey, e);
            }
        }

        // Add headers for fresh response
        addCustomHeaders(response);
    }

    private void serveCachedResponse(HttpServletResponse response, Cache.ValueWrapper cachedValue, String cacheKey)
            throws IOException {
        logger.info("Cache hit for key: {}", cacheKey);
        response.setContentType("application/json; charset=UTF-8");
        addCustomHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write((String) Objects.requireNonNull(cachedValue.get()));
    }

    private void addCustomHeaders(HttpServletResponse response) {
        response.setHeader("X-Cache", "HIT");
        response.setHeader("Cache-Control", "max-age=3600, must-revalidate");
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        logger.debug("Custom headers added to the response");
    }

    private boolean shouldCacheRequest(HttpServletRequest request) {
        // Only cache GET requests for JSON responses
        return "GET".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().contains("/api/");
    }

    private String generateCacheKey(HttpServletRequest request) {
        String rawKey = String.format("%s::%s::%s",
                cacheManagerName,
                request.getRequestURI(),
                Optional.ofNullable(request.getQueryString()).orElse("")
        );
        return DigestUtils.md5DigestAsHex(rawKey.getBytes(StandardCharsets.UTF_8));
    }

    // Extract a generalized key for the entity (e.g., product/123)
    private String extractEntityKey(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        if (parts.length > 1) {
            // Assuming entity name and ID format: /entity/id
            return parts[1] + (parts.length > 2 ? "/" + parts[2] : "");
        }
        return uri;
    }
}