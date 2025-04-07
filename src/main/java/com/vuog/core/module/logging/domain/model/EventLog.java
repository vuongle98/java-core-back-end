package com.vuog.core.module.logging.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_logs")
@ToString
public class EventLog extends BaseModel {

    private String name;
    private String payload;
}
