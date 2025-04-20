package com.vuog.core.module.report.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryResponse implements Serializable {
    private String id;
    private String name;
    private long value;
    private double change; // fake data hoặc có logic sau
}