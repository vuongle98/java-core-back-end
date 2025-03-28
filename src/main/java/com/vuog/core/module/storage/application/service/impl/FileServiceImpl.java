package com.vuog.core.module.storage.application.service.impl;

import com.vuog.core.module.storage.application.dto.FileDownloadDto;
import com.vuog.core.module.storage.domain.model.File;
import com.vuog.core.module.storage.domain.repository.FileRepository;
import com.vuog.core.module.storage.application.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

//    @Value("app.storage-location")
    private final String DEFAULT_UPLOAD_DIR = "uploads";

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void init() {
        try {
            Path path = Paths.get(DEFAULT_UPLOAD_DIR);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public File uploadFile(MultipartFile file) throws IOException {

        // init file object to store to database
        File fileInfo = initFile(file, null);

        storeFile(file.getInputStream(), fileInfo.getPath());
        logger.info("Uploaded file: {}", file.getName());

        return fileRepository.save(fileInfo);
    }

    @Override
    public File updateFile(Long id, MultipartFile file) throws IOException {
        File existedFile = fileRepository.getById(id);

        existedFile = initFile(file, existedFile);

        storeFile(file.getInputStream(), existedFile.getPath());
        logger.info("Uploaded file: {}", file.getName());

        return fileRepository.save(existedFile);
    }

    @Override
    public FileDownloadDto downloadFile(Long fileId) throws IOException {

        // get file info
        File fileInfo = fileRepository.getById(fileId);

        Path filePath = Paths.get(fileInfo.getPath());

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + fileInfo.getName());
        }

        FileDownloadDto downloadInfo = new FileDownloadDto();
        downloadInfo.setFileInfo(fileInfo);
        downloadInfo.setFileResource(resource);

        return downloadInfo;
    }

    @Override
    public void deleteFile(Long fileId, boolean force) throws IOException {

        File fileInfo = fileRepository.getById(fileId);

        if (force) {
            fileRepository.delete(fileInfo);
            Path filePath = Paths.get(fileInfo.getPath());
            Files.delete(filePath);
            logger.info("File deleted: {}", filePath);

            return;
        }

        fileInfo.setIsDeleted(true);
        fileRepository.save(fileInfo);
    }

    // for the rest the file name
    private Path getFilePath(String fileName) throws IOException {
        String currentDateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // check if folder exists
        Path dateFolder = Paths.get(DEFAULT_UPLOAD_DIR, currentDateFolder);
        if (!Files.exists(dateFolder)) {
            Files.createDirectories(dateFolder);
            logger.debug("Created new directory: {}", dateFolder);
        }

        return Paths.get(DEFAULT_UPLOAD_DIR, currentDateFolder, fileName);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "unknown" : fileName.substring(dotIndex + 1);
    }

    private File initFile(MultipartFile file, File fileInfo) throws IOException {

        if (Objects.isNull(fileInfo)) {
            fileInfo = new File();
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        fileInfo.setName(fileName);

        String fileExtension = getFileExtension(fileName);
        String contentType = file.getContentType();

        fileInfo.setExtension(fileExtension);
        fileInfo.setSize(file.getSize());
        fileInfo.setContentType(contentType);

        // init file path to store
        Path filePath = getFilePath(fileInfo.getName());
        fileInfo.setPath(filePath.toString());

        return fileInfo;
    }

    private void storeFile(InputStream in, String path) throws IOException {
        Path filePath = Paths.get(path);
        Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
