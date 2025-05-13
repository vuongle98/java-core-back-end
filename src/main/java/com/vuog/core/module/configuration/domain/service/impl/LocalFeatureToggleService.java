package com.vuog.core.module.configuration.domain.service.impl;

import com.vuog.core.module.configuration.domain.model.FeatureFlag;
import com.vuog.core.module.configuration.domain.repository.FeatureFlagRepository;
import com.vuog.core.module.configuration.domain.service.FeatureToggleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocalFeatureToggleService implements FeatureToggleService {

    private final FeatureFlagRepository repository;

    public LocalFeatureToggleService(FeatureFlagRepository repository) {
        this.repository = repository;
    }

    public boolean isFeatureEnabled(String featureName) {
        Optional<FeatureFlag> feature = repository.findByName(featureName);
        return feature.isPresent() && feature.get().getEnabled();
    }

    public boolean isFeatureEnabled(Long id) {
        Optional<FeatureFlag> feature = repository.findById(id);
        return feature.isPresent() && feature.get().getEnabled();
    }

    public FeatureFlag toggleFeature(Long id) {
        Optional<FeatureFlag> feature = repository.findById(id);
        if (feature.isPresent()) {
            FeatureFlag flag = feature.get();
            flag.setEnabled(!flag.getEnabled());
            return repository.save(flag);
        } else {
            return null;
        }
    }

    public FeatureFlag getByKey(String key) {
        Optional<FeatureFlag> feature = repository.findByName(key);
        return feature.orElseThrow(() -> new IllegalArgumentException("Feature not found"));
    }
}
