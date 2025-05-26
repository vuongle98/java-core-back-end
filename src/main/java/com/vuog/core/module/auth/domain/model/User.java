package com.vuog.core.module.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(EntityChangeListener.class)
public class User extends BaseModel implements UserDetails {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    private Boolean locked = false;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column(name = "realm_id")
    private String realmId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Token> tokens = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserProfile profile;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserSetting settings;

    @Column(name = "is_verified_email")
    private Boolean iseVerifiedEmail;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(
            String username,
            String email,
            String password,
            Set<Role> roles
    ) {
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.setRoles(roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add both role and permissions as authorities
        // Use role code instead of name

        Set<String> authorities = new HashSet<>();
        Set<Long> visitedRoles = new HashSet<>(); // Tránh vòng lặp

        // Thu thập từ tất cả Role của User
        for (Role role : roles) {
            collectAuthoritiesFromRole(role, authorities, visitedRoles);
        }

        // Chuyển đổi thành GrantedAuthority
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Đệ quy lấy Role và Permission từ Role và các Role cha
    private void collectAuthoritiesFromRole(Role role, Set<String> authorities, Set<Long> visitedRoles) {
        if (visitedRoles.contains(role.getId())) {
            return; // Ngăn vòng lặp trong kế thừa Role
        }
        visitedRoles.add(role.getId());

        // Thêm tên Role với tiền tố ROLE_
        authorities.add("ROLE_" + role.getCode());

        // Thêm tất cả Permission của Role
        for (Permission perm : role.getPermissions()) {
            authorities.add(perm.getCode());
        }

        // Đệ quy thêm từ các Role cha
        for (Role parentRole : role.getChildRoles()) {
            collectAuthoritiesFromRole(parentRole, authorities, visitedRoles);
        }
    }

    public boolean isSuperAdmin() {
        return this.getRoles().stream().anyMatch(role -> role.getCode().equalsIgnoreCase("SUPER_ADMIN"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked == null || !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return getIsActive();
    }
}
