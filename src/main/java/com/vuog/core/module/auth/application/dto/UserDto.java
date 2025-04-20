package com.vuog.core.module.auth.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.model.User;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto extends BaseDto {
    private String username;
    private String email;
    private Set<String> permissions;
    private Set<String> roles;
    private Boolean locked;
    private Instant createdAt;
    private Instant updatedAt;

    private UserProfileDto profile;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet());
        this.permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream().map(Permission::getCode)).collect(Collectors.toSet());
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public UserDto(String username) {
        this.username = username;
    }
}
