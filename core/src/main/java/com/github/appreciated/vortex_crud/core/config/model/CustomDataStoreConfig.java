package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.util.Map;

/**
 * A builder helper for creating DataStoreConfig instances for custom data stores.
 * Since DataStoreConfig is a concrete class, this utility simplifies its creation
 * with custom VortexCrudDataStore implementations.
 */
public class CustomDataStoreConfig {

    public static <ModelClass, FieldType, RepositoryType> DataStoreConfig.DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> builder(
            VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance,
            Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields
    ) {
        return DataStoreConfig.<ModelClass, FieldType, RepositoryType>builder()
                .dataStoreInstance(dataStoreInstance)
                .fields(fields);
    }
}
