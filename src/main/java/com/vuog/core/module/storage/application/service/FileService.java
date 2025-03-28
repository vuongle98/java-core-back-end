package com.vuog.core.module.storage.application.service;

import com.vuog.core.module.storage.application.dto.FileDownloadDto;
import com.vuog.core.module.storage.domain.model.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileService {

    void init();

    File uploadFile(MultipartFile file) throws IOException;

    File updateFile(Long id, MultipartFile file) throws IOException;

    FileDownloadDto downloadFile(Long fileId) throws IOException;

    void deleteFile(Long fileId, boolean force) throws IOException;
}
