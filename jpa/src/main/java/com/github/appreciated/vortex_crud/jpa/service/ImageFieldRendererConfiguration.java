package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFieldRendererConfiguration {
    Class<? extends VortexCrudResourceProvider> value();
}
