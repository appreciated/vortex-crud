package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionData;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaManyToMany implements ManyToMany<JpaRepository<?, ?>, String> {

    private final String referenceField;

    public JpaManyToMany(String referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<GenericEntity> getData(VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String> dataStoreFactoryRegistry, String foreignKeyValue, VortexCrudDataStore<String> dataStore, CollectionData<JpaRepository<?, ?>, String> collectionData) {
        return foreignKeyValue == null ? List.of() :
                dataStore.getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
    }

    @Override
    public String getReferenceField(CollectionData<JpaRepository<?, ?>, String> collectionData) {
        return "";
    }

    @Override
    public JpaRepository<?, ?> getAssociativeDataStore() {
        return null;
    }

    @Override
    public String getAssociativeTargetIdField() {
        return "";
    }

}
