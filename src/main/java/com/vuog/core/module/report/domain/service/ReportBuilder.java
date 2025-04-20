package com.vuog.core.module.report.domain.service;

import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.domain.model.ReportRequest;

import java.util.List;
import java.util.Map;

public interface ReportBuilder {
    List<Map<String, Object>> buildReport(ReportRequest request);

    List<Map<String, Object>> buildAggregationReport(ReportRequest request);
}