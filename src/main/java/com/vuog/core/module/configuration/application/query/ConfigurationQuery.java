package com.vuog.core.module.configuration.application.query;

import com.vuog.core.module.configuration.domain.model.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationQuery implements Serializable {

    private String key;
    private String value;
    private Configuration.Environment environment;
}
