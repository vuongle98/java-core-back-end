package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.common.exception.UserNotFoundException;
import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.command.ChangePasswordCommand;
import com.vuog.core.module.auth.application.command.ResetPasswordCommand;
import com.vuog.core.module.auth.application.command.UpdateProfileCommand;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.dto.UserProfileDto;
import com.vuog.core.module.auth.application.query.UserQuery;
import com.vuog.core.module.auth.application.service.UserService;
import com.vuog.core.module.auth.application.specification.UserSpecification;
import com.vuog.core.module.auth.domain.event.UserCreatedEvent;
import com.vuog.core.module.auth.domain.event.UserUpdatedEvent;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.model.UserProfile;
import com.vuog.core.module.auth.domain.model.UserSetting;
import com.vuog.core.module.auth.domain.repository.RoleRepository;
import com.vuog.core.module.auth.domain.repository.UserProfileRepository;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import com.vuog.core.module.auth.domain.repository.UserSettingRepository;
import com.vuog.core.module.auth.domain.service.UserDomainService;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserProfileRepository profileRepository;
    private final UserSettingRepository userSettingRepository;


    @Value("${app.user.default.password}")
    private String DEFAULT_PASSWORD;

    public UserServiceImpl(UserDomainService userDomainService,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ApplicationEventPublisher eventPublisher,
                           UserProfileRepository profileRepository,
                           UserSettingRepository userSettingRepository
    ) {
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.profileRepository = profileRepository;
        this.userSettingRepository = userSettingRepository;
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

        // Create and set UserProfile
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);     // If you have a user field in UserProfile
        user.setProfile(userProfile);  // If you have a profile field in User

        // Create and set UserSetting
        UserSetting userSetting = new UserSetting();
        userSetting.setUser(user);
        user.setSettings(userSetting); // If you have a settings field in User

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

        User existedUser = getById(id);
        existedUser.setIsDeleted(true);
        userRepository.save(existedUser);
    }

    @Override
    public UserDto getProfile(Long userId) {
        User user = getById(userId);

        UserProfile userProfile = profileRepository.findByUser(user).orElseThrow(() -> new UserNotFoundException("Profile not found."));

        UserDto userDto = new UserDto(user);
        userDto.setProfile(new UserProfileDto(userProfile));

        return userDto;
    }

    @Override
    public UserDto initProfile(Long userId) {
        User user = getById(userId);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile = profileRepository.save(userProfile);

        user.setProfile(userProfile);
        userRepository.save(user);

        UserDto userDto = new UserDto(user);
        userDto.setProfile(new UserProfileDto(userProfile));

        return userDto;
    }

    @Override
    public UserDto updateProfile(Long userId, UpdateProfileCommand command) {
        User user = getById(userId);

        UserProfile userProfile = profileRepository.findByUser(user).orElseThrow(() -> new UserNotFoundException("Profile not found."));

        if (Objects.nonNull(command.getAddress())) {
            userProfile.setAddress(command.getAddress());
        }

        if (Objects.nonNull(command.getPhone())) {
            userProfile.setPhone(command.getPhone());
        }

        if (Objects.nonNull(command.getFirstName())) {
            userProfile.setFirstName(command.getFirstName());
        }

        if (Objects.nonNull(command.getLastName())) {
            userProfile.setLastName(command.getLastName());
        }

        if (Objects.nonNull(command.getAvatarUrl())) {
            userProfile.setAvatarUrl(command.getAvatarUrl());
        }

        userProfile = profileRepository.save(userProfile);

        UserDto userDto = new UserDto(user);
        userDto.setProfile(new UserProfileDto(userProfile));

        return userDto;
    }

    @Override
    public User changePassword(Long userId, ChangePasswordCommand command) {

        User user = getById(userId);
        String encodeCurrPass = passwordEncoder.encode(command.getCurrentPassword());
        userDomainService.validatePassword(user, encodeCurrPass);

        if (!command.getNewPassword().equals(command.getConfirmPassword())) {
            throw new IllegalArgumentException("Current password and confirm password do not match.");
        }

        String newPassword = passwordEncoder.encode(command.getNewPassword());
        user.setPassword(newPassword);

        return userRepository.save(user);
    }

    @Override
    public void block(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setLocked(!user.getLocked());
        userRepository.save(user);
    }

    @Override
    public void resetPassword(Long userId, ResetPasswordCommand command) {
        User user = getById(userId);

        userDomainService.validateEmail(user, command.getEmail());

        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
    }
}