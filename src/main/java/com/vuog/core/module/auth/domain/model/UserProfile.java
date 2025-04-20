package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "user_profiles")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(EntityChangeListener.class)
public class UserProfile extends BaseModel {

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
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
