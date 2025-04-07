package com.vuog.core.module.configuration.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // Dùng cho phương thức
@Retention(RetentionPolicy.RUNTIME)  // Lưu trữ tại runtime
public @interface FeatureToggle {

    String feature() default "";
}
