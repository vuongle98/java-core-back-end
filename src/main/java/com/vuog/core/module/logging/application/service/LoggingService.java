package com.vuog.core.module.logging.application.service;

import com.vuog.core.module.logging.application.command.CreateUserRequestLogReq;

public interface LoggingService {

    public void logUserRequest(CreateUserRequestLogReq req);
}
