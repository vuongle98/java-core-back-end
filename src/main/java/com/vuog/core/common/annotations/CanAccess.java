package com.vuog.core.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,})
@Retention(RetentionPolicy.RUNTIME)
public @interface CanAccess {

    String role() default "";
    boolean isRole() default false;
}
