package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;

public class JpaDataStoreConfig extends DataStoreConfig<String, String> {
    public JpaDataStoreConfig(Class<? extends TurboCrudDataStore> factory) {
        super((Class<? extends TurboCrudDataStore<String>>) factory);
    }

    public  static DataStoreConfig.Builder<String, String> of(Class<? extends TurboCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<String, String>((Class<? extends TurboCrudDataStore<String>>) factory));
    }

}
