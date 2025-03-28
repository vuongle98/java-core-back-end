package com.vuog.core.module.user.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar_url")
    private String avatarUrl;
}
