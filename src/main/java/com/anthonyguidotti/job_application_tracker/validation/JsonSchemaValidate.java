package com.anthonyguidotti.job_application_tracker.validation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsonSchemaValidate {
    @AliasFor("value")
    String schemaFileLocation() default "";

    @AliasFor("schemaFileLocation")
    String value() default "";
}
