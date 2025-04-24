package com.vuog.core.module.storage.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.common.listener.EntityChangeListener;
import com.vuog.core.module.auth.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
@ToString
@EntityListeners(EntityChangeListener.class)
public class File extends BaseModel implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String path;
    private String name;
    private String extension;
    private String contentType;
    private Long size;
}
