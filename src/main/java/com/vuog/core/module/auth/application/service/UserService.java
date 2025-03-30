package com.vuog.core.module.auth.application.service;

import com.vuog.core.common.base.BaseQueryService;
import com.vuog.core.common.base.BaseService;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.query.UserQuery;
import com.vuog.core.module.auth.domain.model.User;

public interface UserService extends
        BaseQueryService<User, UserQuery>,
        BaseService<User, CreateUserReq> {

}