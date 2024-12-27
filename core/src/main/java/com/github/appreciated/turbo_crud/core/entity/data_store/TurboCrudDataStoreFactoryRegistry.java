package com.github.appreciated.turbo_crud.core.entity.data_store;

public interface TurboCrudDataStoreFactoryRegistry<T> {
    TurboCrudDataStore getFactory(T table);

    void addFactory(T table, TurboCrudDataStore factory);
}
