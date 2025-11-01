package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaDataStoreConfig {
    public static DataStoreConfig.DataStoreConfigBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> of(JpaRepository<?, ?> factory) {
        return DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory(factory);
    }
}
