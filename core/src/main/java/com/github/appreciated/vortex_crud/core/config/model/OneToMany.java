package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public interface OneToMany<DataStoreId, FieldId, KeyType> {

    java.util.Collection<DataStoreId> getData(String foreignKeyValue, VortexCrudDataStore<FieldId, ?> dataStore, CollectionConfiguration<DataStoreId, FieldId, KeyType> collectionConfiguration);

    FieldId getReferenceField(CollectionConfiguration<DataStoreId, FieldId, KeyType> collectionConfiguration);
}
