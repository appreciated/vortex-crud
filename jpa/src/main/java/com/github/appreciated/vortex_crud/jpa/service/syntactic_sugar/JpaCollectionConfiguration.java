package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionConfiguration extends CollectionConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public JpaCollectionConfiguration(JpaRepository<?, ?> factory) {
        super(factory);
    }

    public static class Builder extends CollectionConfiguration.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
        public Builder(CollectionConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> product) {
            super(product);
        }
    }

    public static JpaCollectionConfiguration.Builder of(JpaRepository<?, ?> factory) {
        return new JpaCollectionConfiguration.Builder(new CollectionConfiguration<>(factory));
    }
}