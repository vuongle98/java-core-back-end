package com.vuog.core.module.stream.infrastructure.listener;

import com.vuog.core.module.stream.application.service.EntityChangeService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * JPA listener to track entity changes
 */
@Slf4j
@Component
public class JpaEntityChangeListener {

    private static EntityChangeService entityChangeService;

    @Autowired
    public void init(ApplicationContext applicationContext) {
        // Avoid circular dependency by getting service from context
        JpaEntityChangeListener.entityChangeService = applicationContext.getBean(EntityChangeService.class);
    }

    @PostPersist
    public void postPersist(Object entity) {
        try {
            entityChangeService.trackCreated(entity, getCurrentUser());
        } catch (Exception e) {
            log.error("Error tracking entity creation: {}", e.getMessage(), e);
        }
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        try {
            entityChangeService.trackUpdated(entity, getCurrentUser());
        } catch (Exception e) {
            log.error("Error tracking entity update: {}", e.getMessage(), e);
        }
    }

    @PostRemove
    public void postRemove(Object entity) {
        try {
            entityChangeService.trackDeleted(entity, getCurrentUser());
        } catch (Exception e) {
            log.error("Error tracking entity deletion: {}", e.getMessage(), e);
        }
    }

    /**
     * Get the current authenticated user
     */
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "SYSTEM";
    }
}
