package com.vuog.core.module.user.application.service;

import com.vuog.core.common.base.BaseQueryService;
import com.vuog.core.common.base.BaseService;
import com.vuog.core.module.user.application.command.CreateUserReq;
import com.vuog.core.module.user.application.dto.UserDto;
import com.vuog.core.module.user.application.query.UserQuery;

public interface UserService extends
        BaseQueryService<UserDto, UserQuery>,
        BaseService<UserDto, CreateUserReq> {

}