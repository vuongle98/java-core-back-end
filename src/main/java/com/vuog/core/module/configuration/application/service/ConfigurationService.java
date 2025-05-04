package com.vuog.core.module.configuration.application.service;

import com.vuog.core.module.configuration.application.command.CreateConfigurationCommand;
import com.vuog.core.module.configuration.application.dto.ConfigurationDto;
import com.vuog.core.module.configuration.application.query.ConfigurationQuery;

import java.util.List;

public interface ConfigurationService {

    ConfigurationDto create(CreateConfigurationCommand command);

    List<ConfigurationDto> getSystemConfiguration(ConfigurationQuery query);
}
