package com.vuog.core.module.auth.application.projection;

import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.auth.domain.model.User;

import java.util.Set;

@ProjectionDefinition(name = "userProjection", types = {User.class})
public interface UserProjection {

    Long getId();

    String getUsername();

    String getEmail();

    String getPhone();

    String getAddress();

    Set<RoleProjection> getRoles();
}
