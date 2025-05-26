package com.vuog.core.common.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.common.dto.UserRequestLog;
import com.vuog.core.common.util.ObjectMappingUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestLogEvent implements DomainEvent {

    private UserRequestLog userRequestLog;

    @Override
    public String key() {
        return null;
    }

    @Override
    public String eventName() {
        return "log.userRequestLog";
    }

    @Override
    public String toPayload() {
        Event<UserRequestLog> event = new Event<>(EventType.CREATE, userRequestLog);
        return ObjectMappingUtil.writeAsString(event);
    }
}
