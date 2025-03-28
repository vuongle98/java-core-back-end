package com.vuog.core.module.user.domain.service;

import com.vuog.core.module.user.domain.model.User;
import com.vuog.core.module.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserDomainServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDomainService userDomainService;

    @Test
    void createUser_ShouldReturnUser() {
        // Arrange
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Assert
        assertDoesNotThrow(() -> userDomainService.validateUser(user));
        // Add more specific assertions based on your requirements
    }
} 