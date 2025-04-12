package com.vuog.core.module.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
@ToString(exclude = {"permissions", "users"})
public class Role extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_hierarchy",
            joinColumns = @JoinColumn(name = "parent_role_id"),
            inverseJoinColumns = @JoinColumn(name = "child_role_id")
    )
    private Set<Role> childRoles = new HashSet<>(); // Các role con

    @JsonIgnore
    public boolean isSuperAdminRole() {
        return "SUPER_ADMIN".equalsIgnoreCase(code);
    }

    @JsonIgnore
    public boolean isParentOf(Role other) {
        return this.getChildRoles().contains(other);
    }

    @JsonIgnore
    public boolean isProtected() {
        return isSuperAdminRole();
    }

}