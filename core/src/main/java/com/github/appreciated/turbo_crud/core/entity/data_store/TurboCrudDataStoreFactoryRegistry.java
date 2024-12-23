package com.github.appreciated.turbo_crud.core.entity.data_store;

public interface TurboCrudDataStoreFactoryRegistry {
    TurboCrudDataStore getFactory(String table);

    void addFactory(String table, TurboCrudDataStore factory);
}
