package com.vuog.core.module.logging.application.service.impl;

import com.vuog.core.module.logging.application.command.CreateUserRequestLogReq;
import com.vuog.core.module.logging.application.service.LoggingService;
import com.vuog.core.module.logging.domain.model.UserRequestLog;
import com.vuog.core.module.logging.domain.repository.UserRequestLogRepository;
import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {

    private final UserRequestLogRepository userRequestLogRepository;

    public LoggingServiceImpl(UserRequestLogRepository userRequestLogRepository) {
        this.userRequestLogRepository = userRequestLogRepository;
    }

    @Override
    public void logUserRequest(CreateUserRequestLogReq req) {

        UserRequestLog userRequestLog = new UserRequestLog();
        userRequestLog.setUser(req.getUser());
        userRequestLog.setIpAddress(req.getIpAddress());
        userRequestLogRepository.save(userRequestLog);
    }
}
