package com.vuog.core.module.report.domain.service.impl;

import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.domain.model.ReportRequest;
import com.vuog.core.module.report.domain.service.ReportBuilder;
import com.vuog.core.module.report.infrastructure.persistence.JpaDynamicReportExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportBuilderImpl implements ReportBuilder {

    private final JpaDynamicReportExecutor executor;

    public ReportBuilderImpl(JpaDynamicReportExecutor executor) {
        this.executor = executor;
    }

    @Override
    public List<Map<String, Object>> buildReport(ReportRequest request) {
        return executor.execute(request);
    }

    @Override
    public List<Map<String, Object>> buildAggregationReport(ReportRequest request) {
        return executor.executeAggregation(request);
    }

}