package com.github.appreciated.vortex_crud.core.entity.data_store;

public interface VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> {
    VortexCrudDataStore<FieldId, ?> getDataStore(Class<? extends DataStoreId> table);

    void addFactory(Class<? extends DataStoreId> table, VortexCrudDataStore<FieldId, ?> factory);
}
