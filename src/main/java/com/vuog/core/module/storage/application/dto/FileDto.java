package com.vuog.core.module.storage.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto implements Serializable {

    private Long id;
    private String name;
    private String extension;
    private String contentType;
    private Long size;
    private String path;
}
