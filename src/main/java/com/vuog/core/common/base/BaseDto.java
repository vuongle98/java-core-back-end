package com.vuog.core.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto implements Serializable {

    protected Long id;

//    protected String createdBy;
//    protected String updatedBy;
//    protected LocalDateTime createdAt;
//    protected LocalDateTime updatedAt;
}
