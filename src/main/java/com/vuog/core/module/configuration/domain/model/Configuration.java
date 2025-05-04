package com.vuog.core.module.configuration.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "configurations")
@ToString
@EntityListeners(EntityChangeListener.class)
public class Configuration extends BaseModel implements Serializable {

    private String key;
    private String value;
    private String type;

    @Enumerated(EnumType.STRING)
    private Category category; // general, regional, etc.

    private String service;

    @Enumerated(EnumType.STRING)
    private Environment environment;
    private String title;
    private String description;

    public enum Category {
        GENERAL, REGIONAL, SYSTEM
    }

    public enum Environment {
        ALL, DEV, PROD
    }
}
