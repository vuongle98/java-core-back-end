package com.vuog.core.module.auth.domain.dto;

import com.vuog.core.module.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User events to avoid serialization issues with entities
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;

    /**
     * Create a DTO from a User entity
     */
    public static UserEventDto fromEntity(User user) {
        if (user == null) return null;

        return UserEventDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                // Add other fields needed for events but exclude collections and complex relationships
                .build();
    }
}
