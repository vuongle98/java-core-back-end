package com.vuog.core.common.listener;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.util.Context;
import com.vuog.core.module.logging.domain.event.AuditLogEvent;
import com.vuog.core.module.logging.domain.model.AuditLog;
import jakarta.persistence.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EntityChangeListener<T extends BaseModel> {

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
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(entity.getId().toString());
        auditLog.setEntity(entity.toString());
        auditLog.setUser(Context.getUser());

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
    }
}
