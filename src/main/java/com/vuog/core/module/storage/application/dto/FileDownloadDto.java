package com.vuog.core.module.storage.application.dto;


import com.vuog.core.module.storage.domain.model.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadDto implements Serializable {

    private Resource fileResource;
    private File fileInfo;
}
