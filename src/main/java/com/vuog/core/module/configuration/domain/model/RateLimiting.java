package com.vuog.core.module.configuration.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.auth.domain.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rate_limiting")
public class RateLimiting extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String apiEndpoint;

    private long requestCount;

    private LocalDateTime resetTime;
}
