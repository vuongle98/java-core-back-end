package com.vuog.core.module.configuration.application.command;

import com.vuog.core.module.configuration.domain.model.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConfigurationCommand implements Serializable {

    private String key;
    private String value;
    private Configuration.Environment environment;
    private String description;
    private Configuration.Category category;
    private String service;
    private String type;
}
