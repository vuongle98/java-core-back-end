package com.vuog.core.module.configuration.infrastructure.persistence;

import com.vuog.core.module.configuration.domain.model.FeatureFlag;
import com.vuog.core.module.configuration.domain.repository.FeatureFlagRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureFlagRepositoryImpl extends GenericRepository<FeatureFlag, Long>, FeatureFlagRepository {

    default Class<FeatureFlag> getEntityClass() {
        return FeatureFlag.class;
    }
}
