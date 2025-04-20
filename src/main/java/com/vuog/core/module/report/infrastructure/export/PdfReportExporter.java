package com.vuog.core.module.report.infrastructure.export;

import com.vuog.core.module.report.domain.model.ReportRequest;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component("PDF")
public class PdfReportExporter implements ReportExporter {

    @Override
    public InputStream exportReport(ReportRequest request, List<Map<String, Object>> results) {
        // PDF generation logic
        // For simplicity, assuming you're using a PDF generation library (e.g., iText)
        // The implementation can vary depending on the library you use

        // Create a simple PDF content (just an example)
        String reportContent = "This is a PDF report for " + request.getEntityName();

        // Return the PDF content as InputStream
        return new ByteArrayInputStream(reportContent.getBytes());
    }
}