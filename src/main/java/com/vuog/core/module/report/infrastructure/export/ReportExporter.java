package com.vuog.core.module.report.infrastructure.export;

import com.vuog.core.module.report.domain.model.ReportRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ReportExporter {
    InputStream exportReport(ReportRequest request, List<Map<String, Object>> results) throws IOException;
}