package com.vuog.core.module.configuration.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feature_flags")
@ToString
public class FeatureFlag extends BaseModel implements Serializable {

    private String name;
    private String value;
    private String type;

    @Enumerated(EnumType.STRING)
    private Environment environment;
    private Boolean enabled;
    private String description;
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Category {
        GENERAL, REGIONAL, SYSTEM, FEATURE_FLAG
    }
    public enum Environment {
        ALL, DEV, PROD, STAGING
    }
}
