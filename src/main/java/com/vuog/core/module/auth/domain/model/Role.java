package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Permission> permissions;

}