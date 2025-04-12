package com.vuog.core.module.auth.domain.service;

import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.model.User;

public interface RoleDomainService {

    boolean isAdmin(User user);

    void validateCanModifyRole(Role role, User currentUser);

    void validateCanRemoveInheritedRole(Role roleToRemove, User currentUser);
}
