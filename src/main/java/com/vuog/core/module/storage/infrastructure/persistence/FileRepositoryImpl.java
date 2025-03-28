package com.vuog.core.module.storage.infrastructure.persistence;

import com.vuog.core.module.rest.domain.repository.GenericRepository;
import com.vuog.core.module.storage.domain.model.File;
import com.vuog.core.module.storage.domain.repository.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepositoryImpl extends GenericRepository<File, Long>, FileRepository {

    default Class<File> getEntityClass() {
        return File.class;
    }
}
