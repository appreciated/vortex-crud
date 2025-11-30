package com.github.appreciated.vortex_crud.core.entity.data_store;

public interface VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> {
    VortexCrudDataStore<FieldType, ModelClass> getDataStore(RepositoryType key);

    void addFactory(RepositoryType key, VortexCrudDataStore<FieldType, ModelClass> factory);
}
