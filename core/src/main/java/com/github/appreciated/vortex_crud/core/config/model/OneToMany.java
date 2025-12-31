package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;

public interface OneToMany<ModelClass, FieldType, RepositoryType> {

    java.util.Collection<ModelClass> getData(Object foreignKeyValue, VortexCrudQueryDataStore<FieldType, ?> dataStore, CollectionConfiguration<ModelClass, FieldType, RepositoryType> collectionConfiguration);

    FieldType getReferenceField(CollectionConfiguration<ModelClass, FieldType, RepositoryType> collectionConfiguration);
}
