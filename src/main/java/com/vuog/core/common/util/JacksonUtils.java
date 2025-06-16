package com.vuog.core.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Utility class to access the ObjectMapper from static contexts
 */
@Component
public class JacksonUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JacksonUtils.applicationContext = applicationContext;
    }

    /**
     * Get the properly configured ObjectMapper from the Spring context
     */
    public static ObjectMapper getObjectMapper() {
        if (applicationContext == null) {
            // Fallback to a default mapper when context is not available
            return new ObjectMapper();
        }
        return applicationContext.getBean(ObjectMapper.class);
    }
}
