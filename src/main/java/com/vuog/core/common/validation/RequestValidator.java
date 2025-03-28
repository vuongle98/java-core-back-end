package com.vuog.core.common.validation;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RequestValidator {

    public List<String> validateFilters(Map<String, String> filters) {
        List<String> errors = new ArrayList<>();
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (!StringUtils.hasText(key)) {
                    errors.add("Filter key cannot be empty");
                }
                if (!StringUtils.hasText(value)) {
                    errors.add("Filter value cannot be empty for key: " + key);
                }
            });
        }
        return errors;
    }

    public List<String> validateId(Object id) {
        List<String> errors = new ArrayList<>();
        if (id == null) {
            errors.add("ID cannot be null");
        }
        return errors;
    }

    public List<String> validateEntity(Object entity) {
        List<String> errors = new ArrayList<>();
        if (entity == null) {
            errors.add("Entity cannot be null");
        }
        return errors;
    }

    public List<String> validatePageable(Pageable pageable) {
        List<String> errors = new ArrayList<>();
        if (pageable == null) {
            errors.add("Pageable cannot be null");
        } else {
            if (pageable.getPageNumber() < 0) {
                errors.add("Page number cannot be negative");
            }
            if (pageable.getPageSize() <= 0) {
                errors.add("Page size must be positive");
            }
        }
        return errors;
    }
} 