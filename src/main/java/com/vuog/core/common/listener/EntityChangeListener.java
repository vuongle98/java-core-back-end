package com.vuog.core.common.listener;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.event.AuditLogEvent;
import com.vuog.core.common.util.Context;
import com.vuog.core.module.auth.application.dto.UserDto;
import jakarta.persistence.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityChangeListener<T extends BaseDto> {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EntityChangeListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // Called before the entity is persisted
    @PrePersist
    public void beforePersist(Object entity) {
        // Handle pre-persist logic
        System.out.println("Before Persist: " + entity);
    }

    // Called after the entity is persisted
    @PostPersist
    public void afterPersist(Object entity) {
        // Handle post-persist logic
        System.out.println("After Persist: " + entity);

        Map<String, Object> auditLog = new HashMap<>();
        auditLog.put("entity", entity);
        auditLog.put("action", "PERSIST");
        auditLog.put("timestamp", System.currentTimeMillis());
        auditLog.put("entityName", entity.getClass().getSimpleName());
        auditLog.put("who", new UserDto(Context.getUser()));

        applicationEventPublisher.publishEvent(new AuditLogEvent(auditLog));
    }

    // Called before the entity is updated
    @PreUpdate
    public void beforeUpdate(Object entity) {
        // Handle pre-update logic
        System.out.println("Before Update: " + entity);
    }

    // Called after the entity is updated
    @PostUpdate
    public void afterUpdate(T entity) {
        // Handle post-update logic
        System.out.println("After Update: " + entity);

        Map<String, Object> auditLog = new HashMap<>();
        auditLog.put("entity", entity);
        auditLog.put("action", "UPDATE");
        auditLog.put("timestamp", System.currentTimeMillis());
        auditLog.put("entityName", entity.getClass().getSimpleName());
        auditLog.put("who", new UserDto(Context.getUser()));

        applicationEventPublisher.publishEvent(new AuditLogEvent(auditLog));
    }

    // Called before the entity is removed
    @PreRemove
    public void beforeRemove(Object entity) {
        // Handle pre-remove logic
        System.out.println("Before Remove: " + entity);
    }

    // Called after the entity is removed
    @PostRemove
    public void afterRemove(Object entity) {
        // Handle post-remove logic
        System.out.println("After Remove: " + entity);

        Map<String, Object> auditLog = new HashMap<>();
        auditLog.put("entity", entity);
        auditLog.put("action", "REMOVE");
        auditLog.put("timestamp", System.currentTimeMillis());
        auditLog.put("entityName", entity.getClass().getSimpleName());
        auditLog.put("who", new UserDto(Context.getUser()));

        applicationEventPublisher.publishEvent(new AuditLogEvent(auditLog));
    }
}
