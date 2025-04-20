package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.application.service.ReportingService;
import com.vuog.core.module.report.domain.model.ReportRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/report")
public class ReportController {

    private final ReportingService reportingService;

    public ReportController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/report")
    public CompletableFuture<List<Map<String, Object>>> report(
            @RequestBody ReportRequest request
    ) throws IOException {
        return reportingService.doReport(request);  // page 1, 20 items per page
    }

    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generateReport(
            @RequestBody ReportRequest request
    ) throws IOException {
        request.setReportFormat(request.getReportFormat().toUpperCase());

        InputStream reportStream = reportingService.generateReport(request);

        // Choose correct file extension and content type
        String extension;
        String contentType = switch (request.getReportFormat().toUpperCase()) {
            case "PDF" -> {
                extension = "pdf";
                yield "application/pdf";
            }
            case "XLSX" -> {
                extension = "xlsx";
                yield "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }
            default -> {
                extension = "csv";
                yield "text/csv";
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report." + extension);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(reportStream));
    }

    @PostMapping("/aggregation")
    public ResponseEntity<List<Map<String, Object>>> generateAggregationReport(@RequestBody ReportRequest request) {
        List<Map<String, Object>> report = reportingService.generateAggregationReport(request);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/metrics")
    public ResponseEntity<ApiResponse<List<DashboardSummaryResponse>>> getDashboardSummary(
            @RequestBody ReportRequest request
    ) {
        List<DashboardSummaryResponse> metrics = reportingService.generateDashboardSummary(request);

        return ResponseEntity.ok(ApiResponse.success(metrics));
    }
}
