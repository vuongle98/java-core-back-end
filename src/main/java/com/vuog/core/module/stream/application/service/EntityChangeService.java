package com.vuog.core.module.stream.application.service;

import com.vuog.core.module.stream.domain.event.EntityChangedEvent.ChangeType;
import com.vuog.core.module.stream.domain.service.EntityChangeTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Application service for tracking entity changes
 */
@Service
@RequiredArgsConstructor
public class EntityChangeService {

    private final EntityChangeTracker entityChangeTracker;

    /**
     * Track a created entity
     * @param entity The created entity
     * @param actor The actor who created the entity
     */
    public void trackCreated(Object entity, String actor) {
        entityChangeTracker.trackChange(entity, ChangeType.CREATED, actor);
    }

    /**
     * Track an updated entity
     * @param entity The updated entity
     * @param actor The actor who updated the entity
     */
    public void trackUpdated(Object entity, String actor) {
        entityChangeTracker.trackChange(entity, ChangeType.UPDATED, actor);
    }

    /**
     * Track a deleted entity
     * @param entity The deleted entity
     * @param actor The actor who deleted the entity
     */
    public void trackDeleted(Object entity, String actor) {
        entityChangeTracker.trackChange(entity, ChangeType.DELETED, actor);
    }
}
