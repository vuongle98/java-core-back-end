package com.vuog.core.module.logging.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.auth.domain.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audit_logs")
@ToString
public class AuditLog extends BaseModel {

    @ManyToOne
    private User user;
    private String entity;
    private String entityId;
    private String action;
}
