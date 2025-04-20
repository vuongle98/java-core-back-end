package com.vuog.core.module.report.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {

    private String entityName; // Fully qualified JPA Entity name
    private List<String> fields;
    private Map<String, String> fieldNames = new HashMap<>();
    private Map<String, Object> filters;
    private Map<String, String> aggregations = new HashMap<>();
    private String groupBy;
    private String reportFormat;  // "PDF", "XLSX", "CSV", etc.
    private int page = 0;
    private int size = 10;


    private LocalDate from;
    private LocalDate to;
    private String timestampField;
}
