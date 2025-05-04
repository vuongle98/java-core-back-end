package com.vuog.core.module.auth.application.service;

import com.vuog.core.common.base.BaseQueryService;
import com.vuog.core.common.base.BaseService;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.command.ChangePasswordCommand;
import com.vuog.core.module.auth.application.command.ResetPasswordCommand;
import com.vuog.core.module.auth.application.command.UpdateProfileCommand;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.query.UserQuery;
import com.vuog.core.module.auth.domain.model.User;

public interface UserService extends
        BaseQueryService<User, UserQuery>,
        BaseService<User, CreateUserReq> {

    UserDto updateProfile(Long userId, UpdateProfileCommand command);

    UserDto getProfile(Long userId);

    UserDto initProfile(Long userId);

    User changePassword(Long userId, ChangePasswordCommand command);

    void block(Long userId);

    void resetPassword(Long userId, ResetPasswordCommand command);
}