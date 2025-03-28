package com.vuog.core.module.storage.domain.projection;

import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.storage.domain.model.File;

@ProjectionDefinition(name = "fileProjection", types = { File.class })
public interface FileProjection {

    Long getId();
    String getName();
    String getPath();
    Long getSize();
    String getExtension();
    String getContentType();
}
