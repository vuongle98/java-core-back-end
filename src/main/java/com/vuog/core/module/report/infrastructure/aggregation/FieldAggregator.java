package com.vuog.core.module.report.infrastructure.aggregation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

public interface FieldAggregator {

    /**
     * Applies the aggregation (e.g., COUNT, SUM) on the given field.
     *
     * @param root The root entity in the criteria query.
     * @param cb The CriteriaBuilder instance.
     * @param field The field name to aggregate.
     * @return The aggregation expression.
     */
    Selection<?> apply(Root<?> root, CriteriaBuilder cb, Path<?> field);
}
