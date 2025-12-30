package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;

public interface HasDataStore<FieldType, ModelClass> {
    VortexCrudQueryDataStore<FieldType, ModelClass> dataStoreInstance();
}
