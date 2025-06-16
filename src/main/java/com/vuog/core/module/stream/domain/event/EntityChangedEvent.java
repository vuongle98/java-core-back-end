package com.vuog.core.module.stream.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Event representing changes to domain entities (create/update/delete)
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EntityChangedEvent extends BaseEvent {
    private String entityType;
    private String entityId;
    private ChangeType changeType;
    private String entityData;
    private String actor;

    @Override
    public String getTopic() {
        return "entity." + entityType.toLowerCase() + "." + changeType.name().toLowerCase();
    }

    public enum ChangeType {
        CREATED, UPDATED, DELETED
    }
}
