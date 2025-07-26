package com.github.appreciated.vortex_crud.core.entity.data_store;

public interface VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> {
    VortexCrudDataStore<FieldId, DataStoreId> getDataStore(KeyType table);

    void addFactory(KeyType table, VortexCrudDataStore<FieldId, DataStoreId> factory);
}
