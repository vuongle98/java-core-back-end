package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
@ToString
public class Session extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String token;

    private InetAddress ipAddress;

    private LocalDateTime expireAt;

}
