package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.User;

public interface VortexCrudConfigurationProvider<DataStoreId, FieldId, KeyType, U extends User> {
    Application<DataStoreId, FieldId, KeyType, U> get();
}
