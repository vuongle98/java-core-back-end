package com.vuog.core.module.logging.domain.model;

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
@Table(name = "event_logs")
public class EventLog extends BaseModel {

    private String name;
    private String payload;
}
