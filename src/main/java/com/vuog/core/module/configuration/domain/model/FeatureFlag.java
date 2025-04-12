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
@Table(name = "feature_flags")
@ToString
@EntityListeners({EntityChangeListener.class})
public class FeatureFlag extends BaseModel {

    private String name;
    private String value;
    private Environment environment;
    private Boolean enabled;
    private String description;

    public enum Environment {
        DEVELOPMENT, PRODUCTION, STAGING
    }
}
