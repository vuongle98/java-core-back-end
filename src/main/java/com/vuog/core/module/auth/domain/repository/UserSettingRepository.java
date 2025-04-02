package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.model.UserSetting;

import java.util.List;
import java.util.Optional;

public interface UserSettingRepository extends
        BaseRepository<UserSetting>,
        BaseQueryRepository<UserSetting> {

    Optional<UserSetting> findByKey(String key);

    List<UserSetting> findAllByUser(User user);
}
