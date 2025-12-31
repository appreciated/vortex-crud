package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreAdapter;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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
    private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;
    private ReflectionService<FieldType> reflectionService;

    private Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields;

    @Builder.Default
    private DataStoreHooks<?> hooks = new DataStoreHooks<>();

    @Override
    public VortexCrudQueryDataStore<FieldType, ModelClass> dataStoreInstance() {
        if (dataStoreInstance instanceof VortexCrudQueryDataStore) {
            return (VortexCrudQueryDataStore<FieldType, ModelClass>) dataStoreInstance;
        }
        return new VortexCrudQueryDataStoreAdapter<>(dataStoreInstance, reflectionService);
    }
}
