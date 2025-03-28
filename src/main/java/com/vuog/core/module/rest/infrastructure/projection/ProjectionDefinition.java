package com.vuog.core.module.rest.infrastructure.projection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectionDefinition {
    String name() default "";

    Class<?>[] types() default {};
} 