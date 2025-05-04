package com.vuog.core.module.configuration.application.service.impl;

import com.vuog.core.module.configuration.application.command.CreateConfigurationCommand;
import com.vuog.core.module.configuration.application.dto.ConfigurationDto;
import com.vuog.core.module.configuration.application.query.ConfigurationQuery;
import com.vuog.core.module.configuration.application.service.ConfigurationService;
import com.vuog.core.module.configuration.domain.model.Configuration;
import com.vuog.core.module.configuration.domain.repository.ConfigurationRepository;
import com.vuog.core.module.configuration.domain.service.impl.ConfigurationDomainServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationDomainServiceImpl configurationDomainService;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository, ConfigurationDomainServiceImpl configurationDomainService) {
        this.configurationRepository = configurationRepository;
        this.configurationDomainService = configurationDomainService;
    }

    @Override
    public ConfigurationDto create(CreateConfigurationCommand command) {
        Configuration configuration = new Configuration();

        configuration.setKey(command.getKey());
        configuration.setValue(command.getValue());
        configuration.setEnvironment(command.getEnvironment());
        configuration.setDescription(command.getDescription());
        configuration.setCategory(command.getCategory());
        configuration.setService(command.getService());
        configuration.setType(command.getType());

        configurationDomainService.checkNotNull(configuration);

        configurationDomainService.checkExisted(configuration);

        configuration = configurationRepository.save(configuration);

        return new ConfigurationDto(configuration);
    }


    @Override
    public List<ConfigurationDto> getSystemConfiguration(ConfigurationQuery query) {
        return configurationRepository.findAllByEnvironment(query.getEnvironment())
                .stream()
                .map(ConfigurationDto::new)
                .collect(Collectors.toList());
    }

}
