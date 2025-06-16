package com.vuog.core.module.stream.domain.service;

import com.vuog.core.module.stream.domain.event.EntityChangedEvent.ChangeType;

/**
 * Domain service interface for tracking entity changes
 */
public interface EntityChangeTracker {

    /**
     * Track a change to an entity
     * @param entity The entity that changed
     * @param changeType The type of change (create/update/delete)
     * @param actor The user or system that made the change
     */
    void trackChange(Object entity, ChangeType changeType, String actor);
}
