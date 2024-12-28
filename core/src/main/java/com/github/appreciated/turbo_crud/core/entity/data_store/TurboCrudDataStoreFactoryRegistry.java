package com.github.appreciated.turbo_crud.core.entity.data_store;

public interface TurboCrudDataStoreFactoryRegistry<DataStoreId> {
    TurboCrudDataStore getFactory(DataStoreId table);

    void addFactory(DataStoreId table, TurboCrudDataStore factory);
}
