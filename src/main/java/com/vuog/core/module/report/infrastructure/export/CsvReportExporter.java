package com.vuog.core.module.report.infrastructure.export;

import com.vuog.core.module.report.domain.model.ReportRequest;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("CSV")
public class CsvReportExporter implements ReportExporter {

    @Override
    public InputStream exportReport(ReportRequest request, List<Map<String, Object>> results) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(baos);

        List<String> fields = new ArrayList<>();
        if (request.getFields() != null) {
            fields.addAll(request.getFields());
        }
        if (request.getAggregations() != null) {
            fields.addAll(request.getAggregations().keySet());
        }

        Map<String, String> fieldNames = request.getFieldNames();

        // Write CSV header
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String displayName = fieldNames.getOrDefault(field, field);
            writer.write(escapeCsv(displayName));
            if (i < fields.size() - 1) {
                writer.write(",");
            }
        }
        writer.write("\n");

        // Write rows
        for (Map<String, Object> row : results) {
            for (int i = 0; i < fields.size(); i++) {
                Object value = row.get(fields.get(i));
                writer.write(escapeCsv(value != null ? value.toString() : ""));
                if (i < fields.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
        }

        writer.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}