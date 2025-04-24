package com.vuog.core.module.storage.application.dto;

import com.vuog.core.common.base.BaseDto;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDto extends BaseDto implements Serializable {

    private Long id;
    private String name;
    private String extension;
    private String contentType;
    private Long size;
    private String path;
}
