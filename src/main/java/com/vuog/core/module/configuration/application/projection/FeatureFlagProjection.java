package com.vuog.core.module.configuration.application.projection;

import com.vuog.core.common.base.BaseProjection;
import com.vuog.core.module.configuration.domain.model.FeatureFlag;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;

@ProjectionDefinition(name = "featureFlagProjection", types = { FeatureFlag.class })
public interface FeatureFlagProjection extends BaseProjection {

    Long getId();
    String getName();
    String getValue();
    String getDescription();
    Boolean getEnabled();
}
