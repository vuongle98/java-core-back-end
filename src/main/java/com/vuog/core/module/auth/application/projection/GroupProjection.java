package com.vuog.core.module.auth.application.projection;

import com.vuog.core.common.base.BaseProjection;
import com.vuog.core.module.auth.domain.model.Group;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;

@ProjectionDefinition(name = "groupProjection", types = {Group.class})
public interface GroupProjection extends BaseProjection {

    Long getId();

    String getCode();

    String getName();

    String getDescription();
}
