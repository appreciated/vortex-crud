package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

public interface HasDataStore<FieldType, ModelClass> {
    VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance();
}
