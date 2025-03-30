package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.module.auth.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vuog.core.module.auth.domain.model.User;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found user: " + username));

        // trigger lazy load
        user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).count();
        user.getRoles().stream().flatMap(role -> role.getChildRoles().stream()).forEach(
                child -> child.getPermissions().stream().flatMap(permission -> permission.getRoles().stream()).count()
        );
        return user;
    }
}
