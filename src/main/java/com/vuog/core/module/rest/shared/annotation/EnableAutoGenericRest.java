package com.vuog.core.module.rest.shared.annotation;

import com.vuog.core.module.rest.shared.config.LiteRestAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LiteRestAutoConfiguration.class)
public @interface EnableAutoGenericRest {
}
