package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "endpoint_secure")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EndpointSecure extends BaseModel {

    private String endpointPattern;
    private String method;
    private String authority;
    private Boolean isRole;
}
