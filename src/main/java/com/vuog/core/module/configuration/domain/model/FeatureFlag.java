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
@Table(name = "feature_flags")
public class FeatureFlag extends BaseModel {

    private String name;
    private String value;
    private Boolean enabled;
    private String description;

}
