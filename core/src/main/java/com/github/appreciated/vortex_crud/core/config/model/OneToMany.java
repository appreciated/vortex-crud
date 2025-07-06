package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public interface OneToMany<DataStoreId, FieldId, ModelClass> {

    List<ModelClass> getData(String foreignKeyValue, VortexCrudDataStore<FieldId, ModelClass> dataStore, CollectionConfiguration<DataStoreId, FieldId, ModelClass> collectionConfiguration);

    FieldId getReferenceField(CollectionConfiguration<DataStoreId, FieldId, ModelClass> collectionConfiguration);
}
