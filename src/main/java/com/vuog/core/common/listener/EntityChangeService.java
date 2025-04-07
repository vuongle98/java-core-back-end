package com.vuog.core.common.listener;

import com.vuog.core.common.base.BaseModel;
import org.springframework.stereotype.Service;

@Service
public class EntityChangeService<T extends BaseModel> {

    private final EntityChangeListener<T> entityChangeListener;

    public EntityChangeService(EntityChangeListener<T> entityChangeListener) {
        this.entityChangeListener = entityChangeListener;
    }

    public void handleEntityChange(T entity, String changeType) {
        switch (changeType) {
            case "PERSIST":
                entityChangeListener.afterPersist(entity);
                break;
            case "UPDATE":
                entityChangeListener.afterUpdate(entity);
                break;
            case "REMOVE":
                entityChangeListener.afterRemove(entity);
                break;
            default:
                break;
        }
    }
}
