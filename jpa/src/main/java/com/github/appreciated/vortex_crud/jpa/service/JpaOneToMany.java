package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreAdapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaOneToMany<ModelClass> implements OneToMany<ModelClass, String, JpaRepository<?, ?>> {

    private final String referenceField;

    public JpaOneToMany(String referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<ModelClass> getData(Object foreignKeyValue, VortexCrudDataStore<String, ?> dataStore, CollectionConfiguration<ModelClass, String, JpaRepository<?, ?>> collectionConfiguration) {
        if (foreignKeyValue == null) return List.of();

        VortexCrudQueryDataStore<String, ?> queryDataStore =
                (dataStore instanceof VortexCrudQueryDataStore)
                        ? (VortexCrudQueryDataStore<String, ?>) dataStore
                        : new VortexCrudQueryDataStoreAdapter<>(dataStore);

        return (List<ModelClass>) queryDataStore.getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
    }

    @Override
    public String getReferenceField(CollectionConfiguration<ModelClass, String, JpaRepository<?, ?>> collectionConfiguration) {
        return referenceField;
    }

    public String getReferenceField() {
        return referenceField;
    }
}
