package com.vuog.core.module.notification.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "notifications")
@ToString
@EntityListeners(EntityChangeListener.class)
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
