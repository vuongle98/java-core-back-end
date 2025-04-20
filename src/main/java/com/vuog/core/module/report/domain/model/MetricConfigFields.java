package com.vuog.core.module.report.domain.model;

public record MetricConfigFields(

    String id,
    String name,
    String entityClassName,
    String timestampField // example: "createdAt", "updatedAt" or null
) {}
