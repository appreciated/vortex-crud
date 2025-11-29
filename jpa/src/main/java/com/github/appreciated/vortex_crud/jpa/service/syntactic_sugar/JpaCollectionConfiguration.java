package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionConfiguration extends CollectionConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>>{
    public static CollectionConfiguration.CollectionConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder(DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> config) {
        return CollectionConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(config);
    }
}