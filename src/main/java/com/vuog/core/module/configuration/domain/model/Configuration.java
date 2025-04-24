package com.vuog.core.module.configuration.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
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
    private Environment environment;
    private String title;
    private String description;

    private enum Environment {
        ALL, DEV, PROD
    }
}
