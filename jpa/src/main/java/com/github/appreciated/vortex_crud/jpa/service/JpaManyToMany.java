package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA implementation of the ManyToMany interface.
 * Uses reflection to work with model classes directly.
 */
public record JpaManyToMany<ModelClass>(
        JpaRepository<?, ?> datastore,
        String referenceField
) implements ManyToMany<ModelClass, String, JpaRepository<?, ?>> {

    @Override
    public String referenceField(CollectionConfiguration<ModelClass, String, JpaRepository<?, ?>> collectionConfiguration) {
        return referenceField;
    }

    @Override
    public ModelClass associativeDataStoreKey() {
        return null;
    }

    @Override
    public String associativeTargetIdField() {
        return "";
    }

    @Override
    public String associativeSourceIdField() {
        return "";
    }
}
