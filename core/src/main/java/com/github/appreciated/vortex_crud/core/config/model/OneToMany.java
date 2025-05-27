package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public interface OneToMany<DataStoreId, FieldId> {

    List<GenericEntity> getData(String foreignKeyValue, VortexCrudDataStore<FieldId> dataStore, CollectionData<DataStoreId, FieldId> collectionData);

    FieldId getReferenceField(CollectionData<DataStoreId, FieldId> collectionData);
}
