package com.vuog.core.module.auth.domain.service.impl;

import com.vuog.core.module.auth.domain.service.AuthDomainService;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthDomainServiceImpl implements AuthDomainService {

    private final UserRepository userRepository;

    public AuthDomainServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }
}
