package com.vuog.core.module.auth.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.auth.domain.model.Permission;
import com.vuog.core.module.auth.domain.model.Role;
import com.vuog.core.module.auth.domain.model.User;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto extends BaseDto implements Serializable {
    private String username;
    private String email;
    private Set<RoleDto> roles;
    private Set<PermissionDto> permissions;
    private Boolean locked;
    private Instant createdAt;
    private Instant updatedAt;

    private UserProfileDto profile;

    public UserDto(User user) {
        this.setId(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(RoleDto::new).collect(Collectors.toSet());
        this.permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream().map(PermissionDto::new)).collect(Collectors.toSet());
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
//        if (Objects.nonNull(user.getProfile())) {
//            this.profile = new UserProfileDto(user.getProfile());
//        }
    }

    public UserDto(String username) {
        this.username = username;
    }
}
