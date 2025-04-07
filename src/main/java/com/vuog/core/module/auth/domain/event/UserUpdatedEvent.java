package com.vuog.core.module.auth.domain.event;

import com.vuog.core.common.event.DomainEvent;
import com.vuog.core.module.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserUpdatedEvent implements DomainEvent {

    private User user;

    @Override
    public String key() {
        return user.getId().toString();
    }

    @Override
    public String eventName() {
        return "event.UserUpdatedEvent";
    }

    @Override
    public String toPayload() {
        return user.toString();
    }
}