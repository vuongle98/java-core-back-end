package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
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
