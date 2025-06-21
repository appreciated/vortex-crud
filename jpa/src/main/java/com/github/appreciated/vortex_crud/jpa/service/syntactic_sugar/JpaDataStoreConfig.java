package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaDataStoreConfig extends DataStoreConfig<JpaRepository<?, ?>, String> {
    public JpaDataStoreConfig(Class<? extends VortexCrudDataStore> factory) {
        super((Class<? extends VortexCrudDataStore<String>>) factory);
    }

    public static DataStoreConfig.Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<JpaRepository<?, ?>, String>((Class<? extends VortexCrudDataStore<String>>) factory));
    }
}
