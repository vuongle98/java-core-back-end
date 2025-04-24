package com.vuog.core.module.auth.application.projection;

import com.vuog.core.common.base.BaseProjection;
import com.vuog.core.module.auth.domain.model.UserSetting;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;

@ProjectionDefinition(name = "userSettingProjection", types = {UserSetting.class})
public interface UserSettingProjection extends BaseProjection {

    Long getId();
    UserProjection getUser();
    String getKey();
    String getValue();
}
