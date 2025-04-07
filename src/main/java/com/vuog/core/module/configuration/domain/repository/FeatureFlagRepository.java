package com.vuog.core.module.configuration.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.configuration.domain.model.FeatureFlag;

import java.util.Optional;

public interface FeatureFlagRepository extends
        BaseRepository<FeatureFlag>, BaseQueryRepository<FeatureFlag> {

    Optional<FeatureFlag> findByName(String featureName);
}
