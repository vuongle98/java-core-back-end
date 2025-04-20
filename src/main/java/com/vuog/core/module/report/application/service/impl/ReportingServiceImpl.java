package com.vuog.core.module.report.application.service.impl;


import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.application.service.ReportingService;
import com.vuog.core.module.report.domain.model.MetricConfigFields;
import com.vuog.core.module.report.domain.model.ReportRequest;
import com.vuog.core.module.report.domain.service.DashboardService;
import com.vuog.core.module.report.domain.service.ReportBuilder;
import com.vuog.core.module.report.infrastructure.export.ReportExporter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final ReportBuilder builder;
    private final DashboardService dashboardService;

    private final Map<String, ReportExporter> exporters;

    public ReportingServiceImpl(ReportBuilder builder, DashboardService dashboardService, Map<String, ReportExporter> exporters) {
        this.builder = builder;
        this.dashboardService = dashboardService;
        this.exporters = exporters;
    }

    @Override
    @Async
    public CompletableFuture<List<Map<String, Object>>> doReport(ReportRequest request) {
        List<Map<String, Object>> results = builder.buildReport(request);
        return CompletableFuture.completedFuture(results);
    }

    @Override
    public InputStream generateReport(ReportRequest request) throws IOException {
        // Fetch data
        List<Map<String, Object>> results = builder.buildReport(request);

        // Choose exporter based on report format
        ReportExporter exporter = exporters.get(request.getReportFormat().toUpperCase());
        if (exporter == null) {
            throw new IllegalArgumentException("Unsupported report format: " + request.getReportFormat());
        }

        // Export the report in the selected format
        return exporter.exportReport(request, results);
    }

    @Override
    public List<Map<String, Object>> generateAggregationReport(ReportRequest request) {
        return builder.buildReport(request);
    }

    @Override
    public List<DashboardSummaryResponse> generateDashboardSummary(ReportRequest request) {

        if (Objects.isNull(request.getTimestampField())) {
            request.setTimestampField("createdAt");
        }

        if (Objects.isNull(request.getFrom())) {
            request.setFrom(LocalDate.now().minusDays(7));
        }

        if (Objects.isNull(request.getTo())) {
            request.setTo(request.getFrom().plusDays(7));
        }

        List<MetricConfigFields> configs = List.of(
                new MetricConfigFields("users", "Total Users", "com.vuog.core.module.auth.domain.model.User", request.getTimestampField()),
                new MetricConfigFields("roles", "Roles", "com.vuog.core.module.auth.domain.model.Role", null),
                new MetricConfigFields("permissions", "Permissions", "com.vuog.core.module.auth.domain.model.Permission", null),
                new MetricConfigFields("files", "Files", "com.vuog.core.module.storage.domain.model.File", request.getTimestampField()),
                new MetricConfigFields("notifications", "Notifications", "com.vuog.core.module.notification.domain.model.Notification", request.getTimestampField())
        );

        return configs.stream().map(config -> dashboardService.build(config, request.getFrom(), request.getTo())).toList();
    }
}