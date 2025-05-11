package com.vuog.core.module.configuration.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.configuration.domain.model.FeatureFlag;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureFlagDto extends BaseDto implements Serializable {

    private String name;
    private String value;
    private String type;
    private String title;
    private String description;
    private Boolean enabled;
    private FeatureFlag.Environment environment;
    private FeatureFlag.Category category;

    public FeatureFlagDto(FeatureFlag featureFlag) {
        this.name = featureFlag.getName();
        this.value = featureFlag.getValue();
        this.type = featureFlag.getType();
        this.title = featureFlag.getTitle();
        this.description = featureFlag.getDescription();
        this.environment = featureFlag.getEnvironment();
        this.category = featureFlag.getCategory();
        this.enabled = featureFlag.getEnabled();
    }
}
