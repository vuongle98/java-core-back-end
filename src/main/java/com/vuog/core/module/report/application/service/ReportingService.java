package com.vuog.core.module.report.application.service;

import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.domain.model.ReportRequest;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ReportingService {

    @Async
    CompletableFuture<List<Map<String, Object>>> doReport(ReportRequest request);

    InputStream generateReport(ReportRequest request) throws IOException;

    List<Map<String, Object>> generateAggregationReport(ReportRequest request);

    List<DashboardSummaryResponse> generateDashboardSummary(ReportRequest request);
}
