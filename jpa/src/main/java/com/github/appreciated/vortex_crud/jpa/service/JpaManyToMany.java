package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

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

    @Override
    public VortexCrudDataStore<String, ModelClass> dataStoreInstance() {
        return null;
    }

    @Override
    public OneToMany<ModelClass, String, JpaRepository<?, ?>> oneToMany() {
        return null;
    }

    @Override
    public ManyToMany<ModelClass, String, JpaRepository<?, ?>> manyToMany() {
        return null;
    }

    @Override
    public List<String> children() {
        return Collections.emptyList();
    }
}
