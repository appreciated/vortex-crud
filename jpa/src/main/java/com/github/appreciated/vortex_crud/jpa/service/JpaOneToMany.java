package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.util.List;

public class JpaOneToMany<ModelClass> implements OneToMany<ModelClass, String> {

    private final String referenceField;

    public JpaOneToMany(String referenceField) {
        this.referenceField = referenceField;
    }


    @Override
    public List<ModelClass> getData(String foreignKeyValue, VortexCrudDataStore<String, ?> dataStore, CollectionConfiguration<ModelClass, String> collectionConfiguration) {
        return foreignKeyValue == null ? List.of() :
                (List<ModelClass>) dataStore.getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
    }

    @Override
    public String getReferenceField(CollectionConfiguration<ModelClass, String> collectionConfiguration) {
        return referenceField;
    }

    public String getReferenceField() {
        return referenceField;
    }
}
