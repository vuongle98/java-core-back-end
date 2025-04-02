package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.module.auth.domain.model.UserSetting;
import com.vuog.core.module.auth.domain.repository.UserSettingRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserSettingRepository extends
        GenericRepository<UserSetting, Long>, UserSettingRepository {

    default Class<UserSetting> getEntityClass() {
        return UserSetting.class;
    }
}
