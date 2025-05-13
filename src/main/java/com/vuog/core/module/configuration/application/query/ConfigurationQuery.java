package com.vuog.core.module.configuration.application.query;

import com.vuog.core.common.base.BaseQuery;
import com.vuog.core.module.configuration.domain.model.Configuration;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationQuery extends BaseQuery implements Serializable {

    private String key;
    private String value;
    private Configuration.Environment environment;
}
