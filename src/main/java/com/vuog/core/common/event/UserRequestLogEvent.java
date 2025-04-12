package com.vuog.core.common.event;

import com.vuog.core.common.dto.UserRequestLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestLogEvent implements DomainEvent {

    private UserRequestLog userRequestLog;

    @Override
    public String eventName() {
        return "event.UserRequestLogEvent";
    }

    @Override
    public String toPayload() {
        return userRequestLog.toString();
    }

    @Override
    public String key() {
        return null;
    }
}
