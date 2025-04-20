package com.vuog.core.module.report.infrastructure.aggregation;

import com.vuog.core.module.report.infrastructure.aggregation.impl.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AggregationMapper {

    private final Map<String, FieldAggregator> aggregatorMap = new HashMap<>();

    public AggregationMapper() {
        aggregatorMap.put("count", new CountAggregator());
        aggregatorMap.put("sum", new SumAggregator());
        aggregatorMap.put("avg", new AvgAggregator());
        aggregatorMap.put("max", new MaxAggregator());
        aggregatorMap.put("min", new MinAggregator());
    }

//    public static String extractFieldName(String aggregationKey) {
//        // Expect keys like: countByActive, sumByAmount
//        int byIndex = aggregationKey.indexOf("By");
//        if (byIndex == -1 || byIndex + 2 >= aggregationKey.length()) {
//            throw new IllegalArgumentException("Invalid aggregation key: " + aggregationKey);
//        }
//        String field = aggregationKey.substring(byIndex + 2);
//        return Character.toLowerCase(field.charAt(0)) + field.substring(1);
//    }

    public static String extractAggregationType(String alias) {
        return alias.split("By")[0]; // e.g. count, sum, avg
    }

    public static String extractFieldName(String alias) {
        if (alias.equalsIgnoreCase("countByAll")) return "";
        String match = alias.replaceAll("^[a-zA-Z]+By", ""); // If no "By", returns whole alias
        return camelToDotPath(match); // else resolve normally
    }

    private static String camelToDotPath(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1.$2").toLowerCase(); // UsersId → users.id
    }

    public static FieldAggregator getAggregator(String aggregationType) {
        return switch (aggregationType.toUpperCase()) {
            case "COUNT" -> new CountAggregator();
            case "SUM" -> new SumAggregator();
            case "AVG" -> new AvgAggregator();
            case "MAX" -> new MaxAggregator();
            case "MIN" -> new MinAggregator();
            // Add more aggregation types as needed
            default -> null;
        };
    }
}
