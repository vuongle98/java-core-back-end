package com.vuog.core.module.report.infrastructure.aggregation.impl;

import com.vuog.core.module.report.infrastructure.aggregation.FieldAggregator;
import jakarta.persistence.criteria.*;

public class MaxAggregator implements FieldAggregator {

    @Override
    public Selection<?> apply(Root<?> root, CriteriaBuilder cb, Path<?> field) {
        return cb.max((Expression<? extends Number>) field);
    }
}
