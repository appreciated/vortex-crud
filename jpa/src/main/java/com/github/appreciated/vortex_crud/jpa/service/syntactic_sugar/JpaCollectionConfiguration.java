package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionConfiguration extends CollectionConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>>{
    public static CollectionConfiguration.CollectionConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder(JpaRepository<?, ?> dataStore) {
        return CollectionConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStore(dataStore);
    }
}