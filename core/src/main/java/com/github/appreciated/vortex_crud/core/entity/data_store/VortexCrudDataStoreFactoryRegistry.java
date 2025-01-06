package com.github.appreciated.vortex_crud.core.entity.data_store;

public interface VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> {
    VortexCrudDataStore<FieldId> getFactory(DataStoreId table);

    void addFactory(DataStoreId table, VortexCrudDataStore<FieldId> factory);
}
