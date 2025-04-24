package com.vuog.core.module.auth.application.projection;

import com.vuog.core.common.base.BaseProjection;
import com.vuog.core.module.auth.domain.model.UserProfile;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;

@ProjectionDefinition(name = "userProfileProjection", types = {UserProfile.class})
public interface UserProfileProjection extends BaseProjection {

    Long getId();
    UserProjection getUser();
    String getAddress();
    String getPhone();
    String getEmail();
    String getFirstName();
    String getLastName();
}
