package com.vuog.core.module.configuration.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.configuration.domain.model.Configuration;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationDto extends BaseDto implements Serializable {

    private String key;
    private String value;
    private String description;
    private Configuration.Environment environment;
    private Configuration.Category category;
    private String service;

    public ConfigurationDto(Configuration configuration) {
        this.setId(configuration.getId());
        this.key = configuration.getKey();
        this.value = configuration.getValue();
        this.description = configuration.getDescription();
        this.environment = configuration.getEnvironment();
        this.category = configuration.getCategory();
        this.service = configuration.getService();
        this.setCreatedAt(configuration.getCreatedAt());
        this.setUpdatedAt(configuration.getUpdatedAt());
        this.setCreatedBy(configuration.getCreatedBy());
        this.setUpdatedBy(configuration.getUpdatedBy());
    }
}
