package com.vuog.core.module.auth.application.projection;

import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.auth.domain.model.Permission;

import java.util.Set;

@ProjectionDefinition(name = "permProjection", types = {Permission.class})
public interface PermissionProjection {

    Long getId();

    String getName();

    String getDescription();

    String getCode();

    Set<RoleProjection> getRole();
}
