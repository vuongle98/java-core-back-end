package com.vuog.core.module.storage.domain.model;

import com.vuog.core.common.base.BaseModel;
import com.vuog.core.module.auth.domain.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String path;
    private String name;
    private String extension;
    private String contentType;
    private Long size;
}
