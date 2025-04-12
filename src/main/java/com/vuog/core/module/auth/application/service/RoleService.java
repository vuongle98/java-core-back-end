package com.vuog.core.module.auth.application.service;

import com.vuog.core.module.auth.application.command.UpdateRoleReq;
import com.vuog.core.module.auth.domain.model.Role;

public interface RoleService {

    Role update(Long id, UpdateRoleReq updateRoleReq);

    void delete(Long id);

}