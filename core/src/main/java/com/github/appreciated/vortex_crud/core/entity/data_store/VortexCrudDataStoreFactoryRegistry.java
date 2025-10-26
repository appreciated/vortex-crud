package com.github.appreciated.vortex_crud.core.entity.data_store;

public interface VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> {
    VortexCrudDataStore<FieldType, ModelClass> getDataStore(RepositoryType table);

    void addFactory(RepositoryType table, VortexCrudDataStore<FieldType, ModelClass> factory);
}
