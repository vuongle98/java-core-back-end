package com.vuog.core.module.auth.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Table(name = "user_settings")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners({EntityChangeListener.class})
public class UserSetting extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String key;
    private String value;

}
