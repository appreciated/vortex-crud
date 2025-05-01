package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.springframework.data.repository.CrudRepository;

public class JpaDataStoreConfig extends DataStoreConfig<CrudRepository<?,?>, String> {
    public JpaDataStoreConfig(Class<? extends VortexCrudDataStore> factory) {
        super((Class<? extends VortexCrudDataStore<String>>) factory);
    }

    public  static DataStoreConfig.Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<CrudRepository<?,?>, String>((Class<? extends VortexCrudDataStore<String>>) factory));
    }

}
