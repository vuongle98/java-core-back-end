package com.vuog.core.module.report.domain.service.impl;

import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.domain.model.MetricConfigFields;
import com.vuog.core.module.report.domain.model.ReportRequest;
import com.vuog.core.module.report.domain.service.DashboardService;
import com.vuog.core.module.report.infrastructure.persistence.JpaDynamicReportExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final JpaDynamicReportExecutor executor;

    public DashboardServiceImpl(JpaDynamicReportExecutor executor) {
        this.executor = executor;
    }

    public DashboardSummaryResponse build(MetricConfigFields config, LocalDate currentDate, LocalDate previousDate) {

        long current = queryCount(config.entityClassName(), "countByAll", null);
        long previous = queryCount(config.entityClassName(), "countByAll", Map.of("createdAt$gte", previousDate));

        return new DashboardSummaryResponse(
                config.id(),
                config.name(),
                current,
                calculateChange(current, previous)
        );
    }

    private long queryCount(String entity, String aggregationKey, Map<String, Object> filters) {
        ReportRequest request = new ReportRequest();
        request.setEntityName(entity);
        request.setAggregations(Map.of(aggregationKey, "count"));

        if (filters != null) {
            request.setFilters(parseFilters(filters));
        }

        List<Map<String, Object>> result = executor.execute(request);
        if (!result.isEmpty()) {
            Object value = result.get(0).get(aggregationKey);
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        }
        return 0;
    }

    private Map<String, Object> parseFilters(Map<String, Object> filters) {
        Map<String, Object> parsedFilters = new HashMap<>();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Handle operator-based filtering (e.g., 'createdAt$gte')
            if (key.contains("$")) {
                String[] parts = key.split("\\$");
                String attributeName = parts[0];
                String operator = parts[1];

                switch (operator) {
                    case "gte":
                        parsedFilters.put(attributeName, value);  // Map to greater than or equal filter
                        break;
                    // Add other cases as needed (e.g., "lte", "eq", etc.)
                    default:
                        throw new IllegalArgumentException("Unsupported operator: " + operator);
                }
            } else {
                parsedFilters.put(key, value);
            }
        }
        return parsedFilters;
    }

    private double calculateChange(long current, long previous) {
        if (previous == 0) return current > 0 ? 100.0 : 0.0;
        return ((double) (current - previous) / previous) * 100.0;
    }
}
