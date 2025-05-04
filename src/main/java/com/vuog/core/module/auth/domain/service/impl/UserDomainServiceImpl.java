package com.vuog.core.module.auth.domain.service.impl;

import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.service.UserDomainService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    @Override
    public void validateUser(User user) {
        if (Objects.isNull(user.getUsername()) || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot empty");
        }
    }

    @Override
    public void validatePassword(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Current password does not match");
        }
    }

    @Override
    public void validateEmail(User user, String email) {
        if (!user.getEmail().equals(email)) {
            throw new IllegalArgumentException("Current email does not match");
        }
    }
}
