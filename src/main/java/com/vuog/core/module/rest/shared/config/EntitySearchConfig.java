package com.vuog.core.module.rest.shared.config;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntitySearchConfig {
    // Cache to avoid repeated reflection cost
    private static final Map<Class<?>, List<String>> cache = new ConcurrentHashMap<>();

    public static List<String> getSearchableFields(Class<?> entityClass) {
        return cache.computeIfAbsent(entityClass, cls -> {
            List<String> fields = new ArrayList<>();
            for (Field f : getAllFields(cls)) {
                if (f.getType().equals(String.class)) {
                    fields.add(f.getName());
                }
            }
            return fields;
        });
    }

    // Helper to get all fields, including inherited ones
    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}