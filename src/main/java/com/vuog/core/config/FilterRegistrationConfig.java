package com.vuog.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan
public class FilterRegistrationConfig {

    private final CustomCacheFilter customCacheFilter;

    public FilterRegistrationConfig(CustomCacheFilter customCacheFilter) {
        this.customCacheFilter = customCacheFilter;
    }

    @Bean
    public FilterRegistrationBean<CustomCacheFilter> loggingFilter() {
        FilterRegistrationBean<CustomCacheFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(customCacheFilter);
        filterRegistrationBean.addUrlPatterns("/api/**");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
