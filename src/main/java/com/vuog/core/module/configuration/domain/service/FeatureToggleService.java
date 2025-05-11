package com.vuog.core.module.configuration.domain.service;

import com.vuog.core.module.configuration.domain.model.FeatureFlag;

public interface FeatureToggleService {

    boolean isFeatureEnabled(String featureName);

    boolean isFeatureEnabled(Long id);

    FeatureFlag toggleFeature(Long id);
}
