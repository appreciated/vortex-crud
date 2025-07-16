package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaDataStoreConfig extends DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?> > {
    public JpaDataStoreConfig(JpaRepository<?, ?> factory) {
        super(factory);
    }

    public static DataStoreConfig.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?> > of(JpaRepository<?, ?> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?> >(factory));
    }
}
