package com.vuog.core.module.configuration.domain.service;

import com.vuog.core.module.configuration.application.annotation.FeatureToggle;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    public FeatureToggleAspect(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
    }

    @Before("@annotation(featureToggle)")  // Aspect sẽ kiểm tra mọi phương thức có @FeatureToggle
    public void checkFeatureToggle(FeatureToggle featureToggle) throws Exception {
        String featureName = featureToggle.feature();
        if (!featureToggleService.isFeatureEnabled(featureName)) {
            throw new UnsupportedOperationException("Feature " + featureName + " is not enabled.");
        }
    }
}
