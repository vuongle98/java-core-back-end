package com.vuog.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.module.rest.application.service.GenericRestService;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionDefinition;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionHandler;
import com.vuog.core.module.rest.infrastructure.projection.ProjectionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.vuog.core.module"
})
public class LiteRestAutoConfiguration {

    @Bean
    public ProjectionHandler projectionHandler() {
        return new ProjectionHandler();
    }

    @Bean
    public Class<?>[] projectionInterfaces() {
        return new Class<?>[]{ProjectionDefinition.class};
    }

    @Bean
    public GenericRestService liteRestService(
            ProjectionHandler projectionHandler,
            ProjectionRegistry projectionRegistry,
            ObjectMapper objectMapper,
            ApplicationContext applicationContext) {
        return new GenericRestService(
                projectionHandler,
                projectionRegistry,
                objectMapper,
                applicationContext
        );
    }
}
