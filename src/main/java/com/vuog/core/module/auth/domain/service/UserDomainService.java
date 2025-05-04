package com.vuog.core.module.auth.domain.service;

import com.vuog.core.module.auth.domain.model.User;

public interface UserDomainService {
    void validateUser(User user);

    void validatePassword(User user, String password);

    void validateEmail(User user, String email);
}
