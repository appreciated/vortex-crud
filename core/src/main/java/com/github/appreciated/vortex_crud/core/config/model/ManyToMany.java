package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;

import java.util.List;

public interface ManyToMany<DataStoreId, FieldId> {

    List<GenericEntity> getData(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, String foreignKeyValue, VortexCrudDataStore<FieldId> dataStore, CollectionConfiguration<DataStoreId, FieldId> collectionConfiguration);

    FieldId getReferenceField(CollectionConfiguration<DataStoreId, FieldId> collectionConfiguration);

    DataStoreId getAssociativeDataStore();

    FieldId getAssociativeTargetIdField();

    FieldId getAssociativeSourceIdField();

}
