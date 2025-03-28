package com.vuog.core.module.auth.application.projection;

import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.auth.domain.model.Role;

import java.util.Set;

@ProjectionDefinition(name = "roleProjection", types = {Role.class})
public interface RoleProjection {

    Long getId();

    String getName();

    String getDescription();

    String getCode();

    Set<PermissionProjection> getPermissions();
}
