package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollectionConfiguration extends CollectionConfiguration<JpaRepository<?, ?>, String> {

    public JpaCollectionConfiguration(JpaRepository<?, ?> factory) {
        super(factory);
    }

    public static class Builder extends CollectionConfiguration.Builder<JpaRepository<?, ?>, String> {
        public Builder(CollectionConfiguration<JpaRepository<?, ?>, String> product) {
            super(product);
        }

        public static JpaCollectionConfiguration.Builder of(JpaRepository<?, ?> factory) {
            return new JpaCollectionConfiguration.Builder(new CollectionConfiguration<>(factory));
        }
    }
}