package com.vuog.core.module.storage.domain.projection;

import com.vuog.core.common.base.BaseProjection;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.storage.domain.model.File;

@ProjectionDefinition(name = "fileProjection", types = { File.class })
public interface FileProjection extends BaseProjection {

    Long getId();
    String getName();
    String getPath();
    Long getSize();
    String getExtension();
    String getContentType();
}
