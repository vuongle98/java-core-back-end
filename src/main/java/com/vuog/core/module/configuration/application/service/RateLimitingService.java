package com.vuog.core.module.configuration.application.service;

public interface RateLimitingService {

    boolean isAllowed(Long userId);
}
