package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.common.exception.UserNotFoundException;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.query.UserQuery;
import com.vuog.core.module.auth.application.service.UserService;
import com.vuog.core.module.auth.application.specification.UserSpecification;
import com.vuog.core.module.auth.domain.event.UserCreatedEvent;
import com.vuog.core.module.auth.domain.event.UserUpdatedEvent;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import com.vuog.core.module.auth.domain.service.UserDomainService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserDomainService userDomainService,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ApplicationEventPublisher eventPublisher
    ) {
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Page<User> findAll(UserQuery query, Pageable pageable) {
        Specification<User> spec = UserSpecification.withFilter(query);

        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User getById(Long id) {
        return findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll(UserQuery query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User create(CreateUserReq req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (Objects.nonNull(req.getRoleIds()) && !req.getRoleIds().isEmpty()) {
            for (Long roleId : req.getRoleIds()) {
                Role role = roleRepository.getById(roleId);
                roles.add(role);
            }
        } else {
            // default roles
            Role role = roleRepository.getById(1L);
            roles.add(role);
        }

        user.setRoles(roles);

        userDomainService.validateUser(user);

        eventPublisher.publishEvent(new UserCreatedEvent(user));

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, CreateUserReq command) {
        User existedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!userRepository.existsByEmail(command.getEmail())) {
            existedUser.setEmail(command.getEmail());
        }

        Set<Role> roles = new HashSet<>();
        for (Long roleId : command.getRoleIds()) {
            Role role = roleRepository.getById(roleId);
            roles.add(role);
        }
        existedUser.setRoles(roles);

        eventPublisher.publishEvent(new UserUpdatedEvent(existedUser));
        return userRepository.save(existedUser);
    }

    @Override
    public void delete(Long id, boolean force) {
        if (force) {
            userRepository.deleteById(id);
            return;
        }

        User existedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        existedUser.setIsDeleted(true);
        userRepository.save(existedUser);
    }
}