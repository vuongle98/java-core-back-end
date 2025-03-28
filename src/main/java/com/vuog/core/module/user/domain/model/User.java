package com.vuog.core.module.user.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.auth.domain.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    private UserProfile userProfile;

}
