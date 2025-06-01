package com.github.appreciated.vortex_crud.jpa.service.datastore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferenceFieldConfiguration {
    String value();

    String[] fields() default {};
}
