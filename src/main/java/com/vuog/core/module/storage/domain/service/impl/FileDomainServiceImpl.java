package com.vuog.core.module.storage.domain.service.impl;

import com.vuog.core.common.exception.FileStorageException;
import com.vuog.core.module.storage.domain.model.File;
import com.vuog.core.module.storage.domain.service.FileDomainService;
import org.springframework.stereotype.Service;

@Service
public class FileDomainServiceImpl implements FileDomainService {


    @Override
    public boolean validateFile(File fileInfo) {

        if (fileInfo.getName().contains("..")) {
            throw new FileStorageException("Invalid file name: " + fileInfo.getName());
        }

        return true;
    }
}
