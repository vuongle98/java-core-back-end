package com.vuog.core.module.configuration.infrastructure.persistence;

import com.vuog.core.module.configuration.domain.model.Configuration;
import com.vuog.core.module.configuration.domain.repository.ConfigurationRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepositoryImpl extends
        GenericRepository<Configuration, Long>,
        ConfigurationRepository {

    default Class<Configuration> getEntityClass() {
        return Configuration.class;
    }
}
