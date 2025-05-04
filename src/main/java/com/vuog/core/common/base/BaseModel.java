package com.vuog.core.common.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
