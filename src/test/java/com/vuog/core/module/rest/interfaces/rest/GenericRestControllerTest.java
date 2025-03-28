package com.vuog.core.module.rest.interfaces.rest;

import com.vuog.core.common.dto.ApiRequest;
import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.common.validation.RequestValidator;
import com.vuog.core.module.rest.application.service.GenericRestService;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenericRestControllerTest {

    @Mock
    private GenericRestService genericRestService;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private GenericRepository<Object, Object> repository;

    @InjectMocks
    private GenericRestController<Object, Object> controller;

    private static final String ENTITY_NAME = "testEntity";
    private static final Object TEST_ID = 1L;
    private Map<String, Object> testData;

    @BeforeEach
    void setUp() {
        testData = new HashMap<>();
        testData.put("name", "Test");
        testData.put("value", 123);
        testData.put("id", 1);
    }

    @Test
    void getAll_ShouldReturnPageOfEntities() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<Object> content = Arrays.asList(new Object(), new Object());
        Page<Object> page = new PageImpl<>(content, pageable, content.size());
        
        when(requestValidator.validatePageable(any())).thenReturn(List.of());
        when(genericRestService.getJpaRepository(ENTITY_NAME)).thenReturn(repository);
        when(genericRestService.findAll(any(), any())).thenReturn(page);

        // Act
        ResponseEntity<ApiResponse<Page<Object>>> response = controller.getAll(ENTITY_NAME, null, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().getContent().size());
    }

    @Test
    void getById_ShouldReturnEntity() {
        // Arrange
        Object testEntity = new Object();
        
        when(requestValidator.validateId(any())).thenReturn(List.of());
        when(genericRestService.getJpaRepository(ENTITY_NAME)).thenReturn(repository);
        when(genericRestService.getById(any(), any())).thenReturn(testEntity);

        // Act
        ResponseEntity<ApiResponse<Object>> response = controller.getById(ENTITY_NAME, TEST_ID, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testEntity, response.getBody().getData());
    }

    @Test
    void create_ShouldReturnCreatedEntity() throws Exception {
        // Arrange
        Object createdEntity = new Object();
        
        when(requestValidator.validateEntity(any())).thenReturn(new ArrayList<>());
        when(genericRestService.getJpaRepository(ENTITY_NAME)).thenReturn(repository);
        when(genericRestService.create(any(), any(), any())).thenReturn(createdEntity);

        // Act
        ResponseEntity<ApiResponse<Object>> response = controller.create(ENTITY_NAME, testData, null);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdEntity, response.getBody().getData());
    }

    @Test
    void update_ShouldReturnUpdatedEntity() throws Exception {
        // Arrange
        Object updatedEntity = new Object();
        
        when(requestValidator.validateId(any())).thenReturn(new ArrayList<>());
        when(requestValidator.validateEntity(any())).thenReturn(new ArrayList<>());
        when(genericRestService.getJpaRepository(ENTITY_NAME)).thenReturn(repository);
        when(genericRestService.update(any(), any(), any(), any())).thenReturn(updatedEntity);

        // Act
        ResponseEntity<ApiResponse<Object>> response = controller.update(ENTITY_NAME, TEST_ID, testData, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedEntity, response.getBody().getData());
    }

    @Test
    void delete_ShouldReturnSuccess() {
        // Arrange
        when(requestValidator.validateId(any())).thenReturn(List.of());
        when(genericRestService.getJpaRepository(ENTITY_NAME)).thenReturn(repository);

        // Act
        ResponseEntity<ApiResponse<Void>> response = controller.delete(ENTITY_NAME, TEST_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Deleted successfully", response.getBody().getMessage());
    }

    @Test
    void searchWithFilters_ShouldReturnFilteredPage() throws Exception {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<Object> content = Arrays.asList(new Object(), new Object());
        Page<Object> page = new PageImpl<>(content, pageable, content.size());
        
        ApiRequest<Object> request = new ApiRequest<>();
        request.setPageable(pageable);
        request.setFilters(new HashMap<>());
        
        when(requestValidator.validateFilters(any())).thenReturn(new ArrayList<>());
        when(requestValidator.validatePageable(any())).thenReturn(new ArrayList<>());
        when(genericRestService.getJpaRepository(ENTITY_NAME)).thenReturn(repository);
        when(genericRestService.findAll(repository, new HashMap<>(), pageable)).thenReturn(page);

        // Act
        ResponseEntity<ApiResponse<Page<Object>>> response = controller.searchWithFilters(ENTITY_NAME, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().getContent().size());
    }
} 