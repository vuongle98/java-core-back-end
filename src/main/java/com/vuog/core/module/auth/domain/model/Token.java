package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
@ToString
@EntityListeners(EntityChangeListener.class)
public class Token extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private LocalDateTime expireAt;

    private LocalDateTime issuedAt;

    @Column(name = "is_black_listed")
    private Boolean isBlacklisted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Token relatedToken;

    public enum TokenType {
        ACCESS, REFRESH
    }

}
