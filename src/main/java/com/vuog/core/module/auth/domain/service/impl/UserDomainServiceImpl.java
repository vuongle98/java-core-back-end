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
}
