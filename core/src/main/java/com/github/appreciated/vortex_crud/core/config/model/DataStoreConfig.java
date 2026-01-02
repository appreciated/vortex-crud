package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DataStoreConfig<ModelClass, FieldType, RepositoryType> implements HasDataStore<FieldType, ModelClass>, ValidatableConfiguration {

    private RepositoryType factory;
    @lombok.NonNull
    private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;

    @lombok.NonNull
    private Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields;

    @Builder.Default
    private DataStoreHooks<?> hooks = new DataStoreHooks<>();
}
