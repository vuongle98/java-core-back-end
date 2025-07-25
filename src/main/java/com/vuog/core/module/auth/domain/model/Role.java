package com.vuog.core.module.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Role extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column(name = "realm_id")
    private String realmId;

    @Column(name = "client_id") // Null for realm roles, populated for client roles
    private String clientId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
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