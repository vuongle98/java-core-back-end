package com.vuog.core.module.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
@Table(name = "groups")
public class Group extends BaseModel implements Serializable {

    private String code;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Role> roles;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "parent_path")
    private String parentPath;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column(name = "realm_id", nullable = false)
    private String realmId;
}
