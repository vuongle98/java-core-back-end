package com.vuog.core.module.auth.application.projection;

import com.vuog.core.common.base.BaseProjection;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.auth.domain.model.User;

import java.util.Set;

@ProjectionDefinition(name = "userProjection", types = {User.class})
public interface UserProjection extends BaseProjection {

    Long getId();

    String getUsername();

    String getEmail();

    String getPhone();

    String getAddress();

    Set<RoleProjection> getRoles();
}
