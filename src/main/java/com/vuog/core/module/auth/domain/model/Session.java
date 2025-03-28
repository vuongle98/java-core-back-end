package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.user.domain.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
public class Session extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String token;

    private InetAddress ipAddress;

    private LocalDateTime expireAt;

}
