package com.vuog.core.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMappingUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeAsString(Object obj) {

        try {
            objectMapper.registerModule(new JavaTimeModule());  // Register the JavaTimeModule
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write object as string: " + obj, e);
        }
    }

}
