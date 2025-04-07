package com.vuog.core.module.logging.domain.model;


import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.auth.domain.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.net.InetAddress;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_request_logs")
@ToString
public class UserRequestLog extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private InetAddress ipAddress;

    private String uri;

    private String method;

    private String payload;

    private String queryString;

}
