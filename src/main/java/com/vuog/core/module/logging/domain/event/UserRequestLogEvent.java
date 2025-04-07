package com.vuog.core.module.logging.domain.event;

import com.vuog.core.common.event.DomainEvent;
import com.vuog.core.module.logging.domain.model.UserRequestLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRequestLogEvent implements DomainEvent {

    private UserRequestLog requestLog;

    @Override
    public String eventName() {
        return "event.UserRequestLogEvent";
    }

    @Override
    public String toPayload() {
        return requestLog.toString();
    }

    @Override
    public String key() {
        return null;
    }
}
