package com.vuog.core.module.configuration.domain.service.impl;

import com.vuog.core.module.configuration.domain.model.Configuration;
import com.vuog.core.module.configuration.domain.repository.ConfigurationRepository;
import com.vuog.core.module.configuration.domain.service.ConfigurationDomainService;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationDomainServiceImpl implements ConfigurationDomainService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationDomainServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public void checkExisted(Configuration configuration) {
        boolean existed = configurationRepository
                .existsByKeyAndEnvironmentAndCategoryAndService(
                        configuration.getKey(),
                        configuration.getEnvironment(),
                        configuration.getCategory(),
                        configuration.getService());

        if (existed) {
            throw new IllegalArgumentException("Configuration already existed");
        }
    }

    @Override
    public void checkNotNull(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }

        if (configuration.getKey() == null) {
            throw new IllegalArgumentException("Configuration key cannot be null");
        }

        if (configuration.getValue() == null) {
            throw new IllegalArgumentException("Configuration value cannot be null");
        }

        if (configuration.getType() == null) {
            throw new IllegalArgumentException("Configuration type cannot be null");
        }

        if (configuration.getEnvironment() == null) {
            throw new IllegalArgumentException("Configuration environment cannot be null");
        }

        if (configuration.getCategory() == null) {
            throw new IllegalArgumentException("Configuration category cannot be null");
        }

        if (configuration.getService() == null) {
            throw new IllegalArgumentException("Configuration service cannot be null");
        }
    }
}
