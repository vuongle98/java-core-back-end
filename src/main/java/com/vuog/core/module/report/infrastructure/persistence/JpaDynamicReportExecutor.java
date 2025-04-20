package com.vuog.core.module.report.infrastructure.persistence;

import com.vuog.core.module.report.domain.model.ReportRequest;
import com.vuog.core.module.report.infrastructure.aggregation.AggregationMapper;
import com.vuog.core.module.report.infrastructure.aggregation.FieldAggregator;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JpaDynamicReportExecutor {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Main execution method that handles both regular fields and aggregations dynamically.
     */
    public List<Map<String, Object>> execute(ReportRequest request) {
        try {
            Class<?> entityClass = Class.forName(request.getEntityName());
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<?> root = query.from(entityClass);

            List<Selection<?>> selections = new ArrayList<>();
            List<Expression<?>> groupByExpressions = new ArrayList<>();
            Map<String, Join<?, ?>> joins = new HashMap<>();

            boolean hasAggregation = request.getAggregations() != null && !request.getAggregations().isEmpty();

            // Process normal fields
            if (request.getFields() != null) {
                for (String field : request.getFields()) {
                    Path<?> path = resolvePath(root, field, joins);
                    selections.add(path.alias(field));
                    if (hasAggregation) {
                        groupByExpressions.add(path);
                    }
                }
            }

            // Process aggregations
            if (hasAggregation) {
                for (String alias : request.getAggregations().keySet()) {
                    String aggregationType = AggregationMapper.extractAggregationType(alias); // e.g. count
                    String fieldName = AggregationMapper.extractFieldName(alias); // e.g. users.id

                    FieldAggregator aggregator = AggregationMapper.getAggregator(aggregationType);
                    if (aggregator != null) {
                        if (fieldName.isEmpty()) {
                            // COUNT(*) on root
                            selections.add(cb.count(root).alias(alias));
                        } else {
                            Path<?> path = resolvePath(root, fieldName, joins);
                            if (!isNumericAggregation(aggregationType) || Number.class.isAssignableFrom(path.getJavaType())) {
                                selections.add(aggregator.apply(root, cb, path).alias(alias));
                            } else {
                                throw new IllegalArgumentException("Field '" + fieldName + "' must be numeric for aggregation: " + aggregationType);
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Unsupported aggregator: " + aggregationType);
                    }
                }
            }

            query.multiselect(selections);

            // WHERE clause
//            if (request.getFilters() != null && !request.getFilters().isEmpty()) {
//                List<Predicate> predicates = new ArrayList<>();
//                for (Map.Entry<String, Object> entry : request.getFilters().entrySet()) {
//                    Path<?> filterPath = resolvePath(root, entry.getKey(), joins);
//                    predicates.add(cb.equal(filterPath, entry.getValue()));
//                }
//                query.where(predicates.toArray(new Predicate[0]));
//            }

            if (request.getFilters() != null && !request.getFilters().isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, Object> entry : request.getFilters().entrySet()) {
                    Predicate predicate = createPredicate(cb, root, entry.getKey(), entry.getValue(), joins);
                    predicates.add(predicate);
                }
                query.where(predicates.toArray(new Predicate[0]));
            }

            // GROUP BY if aggregation exists
            if (hasAggregation && !groupByExpressions.isEmpty()) {
                query.groupBy(groupByExpressions);
            }

            TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);

            if (!hasAggregation) {
                typedQuery.setFirstResult(request.getPage() * request.getSize());
                typedQuery.setMaxResults(request.getSize());
            }

            List<Map<String, Object>> result = new ArrayList<>();
            for (Tuple tuple : typedQuery.getResultList()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (TupleElement<?> element : tuple.getElements()) {
                    row.put(element.getAlias(), tuple.get(element));
                }
                result.add(row);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute report: " + request.getEntityName(), e);
        }
    }

    public List<Map<String, Object>> executeAggregation(ReportRequest request) {
        try {
            Class<?> entityClass = Class.forName(request.getEntityName());
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<?> root = query.from(entityClass);

            // List of dynamic aggregations based on the input
            List<Selection<?>> selections = new ArrayList<>();
            List<String> aggregationLabels = new ArrayList<>(); // Store aggregation labels

            Map<String, Join<?, ?>> joins = new HashMap<>();

            // Handle selected fields
            if (request.getFields() != null) {
                for (String field : request.getFields()) {
                    Path<?> path = resolvePath(root, field, joins);
                    selections.add(path.alias(field));
                }
            }

            // Apply dynamic aggregations based on the requested fields and their aggregation types
            for (Map.Entry<String, String> aggregation : request.getAggregations().entrySet()) {
                String field = aggregation.getKey();
                String aggregationType = aggregation.getValue();

                Path<?> path = resolvePath(root, field, joins);

                FieldAggregator aggregator = AggregationMapper.getAggregator(aggregationType);
                if (aggregator != null) {
                    selections.add(aggregator.apply(root, cb, path));
                    aggregationLabels.add(field); // Add the label for aggregation
                } else {
                    throw new IllegalArgumentException("Invalid aggregation type: " + aggregationType);
                }
            }

            query.multiselect(selections);

            // Handle filters if provided
            if (request.getFilters() != null) {
                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, Object> filter : request.getFilters().entrySet()) {
                    predicates.add(cb.equal(root.get(filter.getKey()), filter.getValue()));
                }
                query.where(predicates.toArray(new Predicate[0]));
            }

            // Handle groupBy if provided
            if (request.getGroupBy() != null) {
                query.groupBy(root.get(request.getGroupBy()));
            }

            // Pagination logic
            TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(request.getPage() * request.getSize());
            typedQuery.setMaxResults(request.getSize());

            // Execute query and map results
            List<Map<String, Object>> result = new ArrayList<>();
            for (Tuple tuple : typedQuery.getResultList()) {
                Map<String, Object> map = new LinkedHashMap<>();
                // Populate the map with the aggregation labels and corresponding values
                for (int i = 0; i < selections.size(); i++) {
                    map.put(aggregationLabels.get(i), tuple.get(i));
                }
                result.add(map);
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute dynamic aggregation query", e);
        }
    }

    private boolean isNumericAggregation(String aggregationType) {
        return Set.of("SUM", "AVG", "MIN", "MAX").contains(aggregationType.toUpperCase());
    }

    private Path<?> resolvePath(From<?, ?> root, String fieldPath, Map<String, Join<?, ?>> joins) {
        String[] parts = fieldPath.split("\\.");
        Path<?> path = root;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i < parts.length - 1) {
                String joinKey = String.join(".", Arrays.copyOfRange(parts, 0, i + 1));
                if (!joins.containsKey(joinKey)) {
                    joins.put(joinKey, ((From<?, ?>) path).join(part, JoinType.LEFT));
                }
                path = joins.get(joinKey);
            } else {
                path = path.get(part);
            }
        }

        return path;
    }

    private Predicate createPredicate(CriteriaBuilder cb, Root<?> root, String field, Object value, Map<String, Join<?, ?>> joins) {
        String[] parts = field.split("\\$");
        String fieldName = parts[0];
        String operator = (parts.length > 1) ? parts[1] : "eq";  // Default to "equal" operator if no operator is specified

        Path<?> path = resolvePath(root, fieldName, joins);

        switch (operator) {
            case "gte":
                return cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) value);
            case "lte":
                return cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) value);
            case "eq":
                return cb.equal(path, value);
            // Handle other operators like "lt", "gt", etc.
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }
}