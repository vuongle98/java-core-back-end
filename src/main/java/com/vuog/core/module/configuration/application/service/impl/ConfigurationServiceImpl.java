package com.vuog.core.module.configuration.application.service.impl;

import com.vuog.core.module.configuration.application.service.ConfigurationService;
import com.vuog.core.module.configuration.domain.model.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {


    @Override
    public Configuration getSystemConfiguration() {
        return null;
    }

}
