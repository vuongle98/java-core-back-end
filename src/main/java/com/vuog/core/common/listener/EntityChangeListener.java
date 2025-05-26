package com.vuog.core.common.listener;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.event.AuditLogEvent;
import com.vuog.core.common.util.Context;
import com.vuog.core.module.auth.application.dto.UserDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class EntityChangeListener {

    private static ApplicationEventPublisher staticPublisher;

    public EntityChangeListener(ApplicationEventPublisher publisher) {
        EntityChangeListener.staticPublisher = publisher;
    }

    private static void publishAudit(Object entity, String action) {
        Map<String, Object> auditLog = new HashMap<>();
//        auditLog.put("entity", entity);
        auditLog.put("action", action);
        auditLog.put("timestamp", System.currentTimeMillis());
        auditLog.put("entityName", entity.getClass().getSimpleName());
        auditLog.put("who", new UserDto(Context.getUser()));

        staticPublisher.publishEvent(new AuditLogEvent(auditLog));
    }

    @PostPersist
    public void afterPersist(Object entity) {
        publishAudit(entity, "PERSIST");
    }

    @PostUpdate
    public void afterUpdate(Object entity) {
        publishAudit(entity, "UPDATE");
    }

    @PostRemove
    public void afterRemove(Object entity) {
        publishAudit(entity, "REMOVE");
    }

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof BaseModel model) {
            model.setCreatedAt(Instant.now());
            model.setUpdatedAt(Instant.now());
            model.setIsDeleted(false);
            model.setIsActive(true);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof BaseModel model) {
            model.setUpdatedAt(Instant.now());
        }
    }
}
