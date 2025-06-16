package com.vuog.core.module.configuration.application.service;

/**
 * Service for rate limiting requests
 */
public interface RateLimitingService {

    /**
     * Check if request is allowed based on rate limiting
     * @param userId User ID or null for anonymous requests
     * @param requestIp IP address of the request
     * @return true if request is allowed, false if limit exceeded
     */
    boolean isAllowed(Long userId, String requestIp);

    /**
     * Get current request count for a user or IP
     * @param userId User ID or null for anonymous requests
     * @param requestIp IP address of the request
     * @return Current request count within the time window
     */
    int getRequestCount(Long userId, String requestIp);

    /**
     * Get configured request limit for a user or anonymous requests
     * @param userId User ID or null for anonymous requests
     * @return Maximum number of requests allowed within the time window
     */
    int getLimit(Long userId);

    /**
     * Get remaining time in seconds for the current rate limit window
     * @param userId User ID or null for anonymous requests
     * @param requestIp IP address of the request
     * @return Remaining time in seconds until rate limit resets
     */
    long getRemainingTimeWindow(Long userId, String requestIp);
}
