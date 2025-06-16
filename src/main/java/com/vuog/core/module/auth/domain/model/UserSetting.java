package com.vuog.core.module.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Table(name = "user_settings")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSetting extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

    private String key;
    private String value;

}
