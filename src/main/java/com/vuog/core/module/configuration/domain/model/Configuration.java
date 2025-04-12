package com.vuog.core.module.configuration.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "configurations")
@ToString
@EntityListeners(EntityChangeListener.class)
public class Configuration extends BaseModel {

    private String key;
    private String value;
    private Environment environment;
    private String description;

    private enum Environment {
        DEV, PROD
    }
}
