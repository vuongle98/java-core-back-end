package com.vuog.core.module.user.domain.service.impl;

import com.vuog.core.module.user.domain.model.User;
import com.vuog.core.module.user.domain.service.UserDomainService;
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
