package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

public class JpaOneToMany<ModelClass> implements OneToMany<ModelClass, String, JpaRepository<?, ?>> {

    private final String referenceField;

    public JpaOneToMany(String referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<ModelClass> getData(Object foreignKeyValue, VortexCrudDataStore<String, ?> dataStore, CollectionConfiguration<ModelClass, String, JpaRepository<?, ?>> collectionConfiguration) {
        if (foreignKeyValue == null) {
            return List.of();
        }
        if (dataStore instanceof VortexCrudQueryDataStore) {
            return (List<ModelClass>) ((VortexCrudQueryDataStore<String, ?>) dataStore).getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
        } else {
            // Fallback: Can't filter by column equals if not queryable.
            // Return empty list or throw exception?
            // Assuming JPA datastores are always QueryDataStore, but interface allows simpler ones.
            // If simpler one is used with OneToMany, it might fail.
            return Collections.emptyList();
        }
    }

    @Override
    public String getReferenceField(CollectionConfiguration<ModelClass, String, JpaRepository<?, ?>> collectionConfiguration) {
        return referenceField;
    }

    public String getReferenceField() {
        return referenceField;
    }
}
