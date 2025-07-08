package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public interface OneToMany<DataStoreId, FieldId> {

    List<DataStoreId> getData(String foreignKeyValue, VortexCrudDataStore<FieldId, ?> dataStore, CollectionConfiguration<DataStoreId, FieldId> collectionConfiguration);

    FieldId getReferenceField(CollectionConfiguration<DataStoreId, FieldId> collectionConfiguration);
}
