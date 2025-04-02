package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "user_settings")
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String key;
    private String value;

}
