package com.vuog.core.module.auth.application.dto;

import com.vuog.core.common.base.BaseDto;
import com.vuog.core.module.auth.domain.model.UserProfile;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto extends BaseDto implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String avatarUrl;

    public UserProfileDto(UserProfile profile) {
        this.id = profile.getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.address = profile.getAddress();
        this.phone = profile.getPhone();
        this.avatarUrl = profile.getAvatarUrl();
    }
}
