package com.vuog.core.common.base;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto implements Serializable {

    private Long id;

    private Instant createdBy;
    private Instant updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
}
