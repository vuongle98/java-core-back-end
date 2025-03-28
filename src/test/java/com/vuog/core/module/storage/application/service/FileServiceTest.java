package com.vuog.core.module.storage.application.service;

import com.vuog.core.module.storage.application.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import com.vuog.core.module.storage.domain.model.File;
import com.vuog.core.module.storage.domain.repository.FileRepository;
import com.vuog.core.module.storage.application.dto.FileDownloadDto;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    private MockMultipartFile testFile;
    private File testFileEntity;

    @BeforeEach
    void setUp() {
        testFile = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello, World!".getBytes()
        );

        testFileEntity = new File();
        testFileEntity.setId(1L);
        testFileEntity.setName("test.txt");
        testFileEntity.setPath("test-uploads/test.txt");
    }

    @Test
    void uploadFile_ShouldReturnFileEntity() throws IOException {
        // Arrange
        when(fileRepository.save(any(File.class))).thenReturn(testFileEntity);

        // Act
        File result = fileService.uploadFile(testFile);

        // Assert
        assertNotNull(result);
        assertEquals(testFileEntity.getId(), result.getId());
        assertEquals(testFileEntity.getName(), result.getName());
        assertEquals(testFileEntity.getPath(), result.getPath());
        verify(fileRepository).save(any(File.class));
    }

    @Test
    void downloadFile_ShouldReturnFileDownloadDto() throws IOException {
        // Arrange
        when(fileRepository.getById(1L)).thenReturn(testFileEntity);
        
        // Mock file system operations
        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class);
             MockedConstruction<UrlResource> mockedUrlResource = mockConstruction(
                 UrlResource.class,
                 (mock, context) -> when(mock.exists()).thenReturn(true)
             )) {
            
            // Mock Path creation
            Path mockPath = mock(Path.class);
            java.net.URI mockUri = java.net.URI.create("file://test.txt");
            when(mockPath.toUri()).thenReturn(mockUri);
            mockedPaths.when(() -> Paths.get(any(String.class))).thenReturn(mockPath);

            // Act
            FileDownloadDto result = fileService.downloadFile(1L);

            // Assert
            assertNotNull(result);
            assertEquals(testFileEntity, result.getFileInfo());
            assertEquals(mockedUrlResource.constructed().get(0), result.getFileResource());
            verify(fileRepository).getById(1L);
            verify(mockPath).toUri();
            verify(mockedUrlResource.constructed().get(0)).exists();
        }
    }

    @Test
    void downloadFile_ShouldThrowException_WhenFileNotFound() {
        // Arrange
        when(fileRepository.getById(1L)).thenReturn(testFileEntity);
        
        // Mock file system operations
        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class);
             MockedConstruction<UrlResource> mockedUrlResource = mockConstruction(
                 UrlResource.class,
                 (mock, context) -> when(mock.exists()).thenReturn(false)
             )) {
            
            // Mock Path creation
            Path mockPath = mock(Path.class);
            java.net.URI mockUri = java.net.URI.create("file://test.txt");
            when(mockPath.toUri()).thenReturn(mockUri);
            mockedPaths.when(() -> Paths.get(any(String.class))).thenReturn(mockPath);

            // Act & Assert
            IOException exception = assertThrows(IOException.class, () -> fileService.downloadFile(1L));
            assertEquals("File not found: " + testFileEntity.getName(), exception.getMessage());
            verify(fileRepository).getById(1L);
            verify(mockPath).toUri();
            
            // Verify that a UrlResource was created and exists() was called
            assertEquals(1, mockedUrlResource.constructed().size());
            verify(mockedUrlResource.constructed().get(0)).exists();
        }
    }

    @Test
    void deleteFile_ShouldDeleteSuccessfully() throws IOException {
        // Arrange
        when(fileRepository.getById(1L)).thenReturn(testFileEntity);
        doNothing().when(fileRepository).delete(testFileEntity);
        
        // Mock file system operations
        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class);
             MockedStatic<java.nio.file.Files> mockedFiles = mockStatic(java.nio.file.Files.class)) {
            
            // Mock Path creation
            Path mockPath = mock(Path.class);
            mockedPaths.when(() -> Paths.get(any(String.class))).thenReturn(mockPath);
            mockedFiles.when(() -> java.nio.file.Files.delete(any(Path.class))).thenAnswer(invocation -> null);

            // Act
            fileService.deleteFile(1L, true);

            // Assert
            verify(fileRepository).getById(1L);
            verify(fileRepository).delete(testFileEntity);
            mockedFiles.verify(() -> java.nio.file.Files.delete(any(Path.class)));
        }
    }

    @Test
    void deleteFile_ShouldThrowException_WhenFileNotFound() {
        // Arrange
        when(fileRepository.getById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> fileService.deleteFile(1L, true));
        verify(fileRepository).getById(1L);
        verify(fileRepository, never()).delete(any(File.class));
    }
} 