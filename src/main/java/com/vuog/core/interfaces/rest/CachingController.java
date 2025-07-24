package com.vuog.core.interfaces.rest;

import com.vuog.core.common.service.RedisCacheService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/caching")
public class CachingController {

    private final RedisCacheService redisCacheService;

    public CachingController(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    @RequestMapping("/clear")
    public void clearCache() {
        redisCacheService.removeAllKeysFromRedis();
    }
}
