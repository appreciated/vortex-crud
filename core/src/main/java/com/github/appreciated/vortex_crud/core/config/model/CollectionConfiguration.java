package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CollectionConfiguration<ModelClass, FieldType, RepositoryType> implements HasDataStore<FieldType, ModelClass> {

    private RepositoryType dataStore;
    private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;

    private OneToMany<ModelClass, FieldType, RepositoryType> oneToMany;

    private ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany;

    private List<FieldType> children;
}