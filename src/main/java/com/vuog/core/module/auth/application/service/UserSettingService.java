package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.application.dto.CreateUserSettingReq;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.model.UserSetting;

import java.util.List;

public interface UserSettingService {

    List<UserSetting> findAllByUser(User user);

    UserSetting getByKey(String key);

    UserSetting create(CreateUserSettingReq req);

    UserSetting updateSetting(String key, String value);
}
