package com.vuog.core.module.configuration.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "configurations")
public class Configuration extends BaseModel {

    private String key;
    private String value;
    private Environment environment;
    private String description;

    private enum Environment {
        DEV, PROD
    }
}
