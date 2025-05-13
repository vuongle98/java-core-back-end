package com.vuog.core.module.auth.application.query;

import com.vuog.core.common.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery extends BaseQuery implements Serializable {

    private Long id;
    private String username;
    private String email;
    private List<Long> rolesIds;
    private String role;
    private String permission;
}