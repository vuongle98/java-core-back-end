package com.vuog.core.module.report.infrastructure.aggregation.impl;

import com.vuog.core.module.report.infrastructure.aggregation.FieldAggregator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

public class CountAggregator implements FieldAggregator {

    @Override
    public Selection<?> apply(Root<?> root, CriteriaBuilder cb, Path<?> field) {
        return cb.count(field);
    }
}
