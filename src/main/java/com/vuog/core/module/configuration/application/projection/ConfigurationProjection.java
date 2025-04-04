package com.vuog.core.module.configuration.application.projection;

import com.vuog.core.module.configuration.domain.model.Configuration;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;

@ProjectionDefinition(name = "configurationProjection", types = { Configuration.class })
public interface ConfigurationProjection {

    Long getId();
    String getKey();
    String getValue();
    String getDescription();
    String getEnvironment();
}
