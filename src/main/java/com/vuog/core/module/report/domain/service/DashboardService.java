package com.vuog.core.module.report.domain.service;

import com.vuog.core.module.report.application.dto.DashboardSummaryResponse;
import com.vuog.core.module.report.domain.model.MetricConfigFields;

import java.time.LocalDate;

public interface DashboardService {

    DashboardSummaryResponse build(MetricConfigFields config, LocalDate currentDate, LocalDate previousDate);
}
