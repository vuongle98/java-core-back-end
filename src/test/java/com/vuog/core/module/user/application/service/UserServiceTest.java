package com.vuog.core.module.user.application.service;

import com.vuog.core.module.auth.application.command.CreateUserReq;
import com.vuog.core.module.auth.application.dto.UserDto;
import com.vuog.core.module.auth.application.service.UserService;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldReturnUserDto() {
        // Arrange
        CreateUserReq request = new CreateUserReq();
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDto result = userService.create(request);

        // Assert
        assertNotNull(result);
        // Add more specific assertions based on your requirements
    }
} 