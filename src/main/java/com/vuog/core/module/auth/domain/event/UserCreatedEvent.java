package com.vuog.core.module.auth.domain.event;

import com.vuog.core.common.event.DomainEvent;
import com.vuog.core.common.event.EventType;
import com.vuog.core.common.event.Event;
import com.vuog.core.common.util.ObjectMappingUtil;
import com.vuog.core.module.auth.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent implements DomainEvent {

    private UserDto user;

    @Override
    public String key() {
        return null; // or whatever makes sense
    }

    @Override
    public String eventName() {
        return "user.events";
    }

    @Override
    public String toPayload() {
        Event<UserDto> event = new Event<>(EventType.CREATE, user);
        return ObjectMappingUtil.writeAsString(event);
    }

}