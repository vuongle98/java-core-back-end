package com.vuog.core.module.user.application.service.impl;

import com.vuog.core.common.exception.UserNotFoundException;
import com.vuog.core.common.util.ObjectMappingUtil;
import com.vuog.core.module.user.application.command.CreateUserReq;
import com.vuog.core.module.user.application.dto.UserDto;
import com.vuog.core.module.user.application.query.UserQuery;
import com.vuog.core.module.user.application.service.UserService;
import com.vuog.core.module.user.application.specification.UserSpecification;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.user.domain.model.User;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import com.vuog.core.module.user.domain.repository.UserRepository;
import com.vuog.core.module.user.domain.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserDomainService userDomainService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<UserDto> findAll(UserQuery query, Pageable pageable) {
        Specification<User> spec = UserSpecification.withFilter(query);

        return ObjectMappingUtil.mapAll(userRepository.findAll(spec, pageable), UserDto.class);
    }

    @Override
    public UserDto getById(Long id) {
        return findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return ObjectMappingUtil.mapNullable(userRepository.findById(id), UserDto.class);
    }

    @Override
    public List<UserDto> getAll(UserQuery query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDto create(CreateUserReq req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());

        Set<Role> roles = new HashSet<>();
        for (Long roleId : req.getRoleIds()) {
            Role role = roleRepository.getById(roleId);
            roles.add(role);
        }

        user.setRoles(roles);

        userDomainService.validateUser(user);

        user = userRepository.save(user);
        return ObjectMappingUtil.map(user, UserDto.class);
    }

    @Override
    public UserDto update(Long id, CreateUserReq command) {
        User existedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));

        existedUser = userRepository.save(existedUser);

        return ObjectMappingUtil.map(existedUser, UserDto.class);
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