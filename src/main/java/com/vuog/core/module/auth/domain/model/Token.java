package com.vuog.core.module.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
@ToString(exclude = {"relatedToken"})
public class Token extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private Instant expireAt;

    private Instant issuedAt = Instant.now();

    @Column(name = "is_black_listed")
    private Boolean isBlacklisted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Token relatedToken;

    public enum TokenType {
        ACCESS, REFRESH
    }

}
