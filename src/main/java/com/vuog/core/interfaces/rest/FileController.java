package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.common.util.ObjectMappingUtil;
import com.vuog.core.module.storage.application.dto.FileDownloadDto;
import com.vuog.core.module.storage.application.dto.FileDto;
import com.vuog.core.module.storage.application.service.FileService;
import com.vuog.core.module.storage.domain.model.File;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FileDto>> uploadFile(
            @RequestParam MultipartFile file
    ) throws IOException {

        File fileInfo = fileService.uploadFile(file);

        FileDto fileDto = ObjectMappingUtil.map(fileInfo, FileDto.class);
        return ResponseEntity.ok(ApiResponse.success(fileDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FileDto>> updateFile(
            @PathVariable Long id,
            @RequestParam MultipartFile file
    ) throws IOException {

        File fileInfo = fileService.updateFile(id, file);

        FileDto fileDto = ObjectMappingUtil.map(fileInfo, FileDto.class);
        return ResponseEntity.ok(ApiResponse.success(fileDto));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id
    ) throws IOException {
        FileDownloadDto downloadInfo = fileService.downloadFile(id);

        String fileNameHeader = "attachment; filename=\"" + downloadInfo.getFileInfo().getName() + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, fileNameHeader)
                .body(downloadInfo.getFileResource());
    }
}
