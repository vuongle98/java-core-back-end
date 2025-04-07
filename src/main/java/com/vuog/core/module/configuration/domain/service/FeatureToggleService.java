package com.vuog.core.module.configuration.domain.service;

public interface FeatureToggleService {

    boolean isFeatureEnabled(String featureName);
}
