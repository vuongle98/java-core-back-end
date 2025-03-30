//package com.vuog.core.module.configuration.application.service.impl;
//
//import com.vuog.core.module.configuration.application.service.RateLimitingService;
//import com.vuog.core.module.configuration.domain.repository.RateLimitingRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class DatabaseRateLimitingService implements RateLimitingService {
//
//    private static final int REQUEST_LIMIT = 10;
//    private static final int TIME_WINDOW = 60; // giây
//
//    private final RateLimitingRepository limitingRepository;
//
//    public DatabaseRateLimitingService(RateLimitingRepository limitingRepository) {
//        this.limitingRepository = limitingRepository;
//    }
//
//
//    @Override
//    public boolean isAllowed(Long userId) {
//        LocalDateTime timeWindowStart = LocalDateTime.now().minusSeconds(TIME_WINDOW);
//        long requestCount = limitingRepository.countRequests(userId, timeWindowStart);
//
//        if (requestCount < REQUEST_LIMIT) {
//            limitingRepository.logRequest(userId, LocalDateTime.now());
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
