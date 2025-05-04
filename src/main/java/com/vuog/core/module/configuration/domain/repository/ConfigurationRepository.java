package com.vuog.core.module.configuration.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.configuration.domain.model.Configuration;

import java.util.List;

public interface ConfigurationRepository extends
        BaseRepository<Configuration>,
        BaseQueryRepository<Configuration> {

    List<Configuration> findAllByEnvironment(Configuration.Environment environment);

    boolean existsByKeyAndEnvironmentAndCategoryAndService(String key, Configuration.Environment environment, Configuration.Category category, String service);
}
