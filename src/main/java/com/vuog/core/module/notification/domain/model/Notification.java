package com.vuog.core.module.notification.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "notifications")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseModel {

    private String title;
    private String content;
    private String channel;
    private String audience;
    private Instant startTime;
    private String status;
}
