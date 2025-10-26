package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;

public interface VortexCrudConfigurationProvider<ModelClass, FieldType, RepositoryType> {
    Application<ModelClass, FieldType, RepositoryType> get();
}
