package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;

public interface VortexCrudConfigurationProvider<DataStoreId, FieldId, ModelClass>  {
    Application<DataStoreId, FieldId, ModelClass>  get();
}
