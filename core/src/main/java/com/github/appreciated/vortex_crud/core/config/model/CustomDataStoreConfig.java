package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.util.Map;

/**
 * A builder for creating a DataStoreConfig with custom DataStore and Field types.
 *
 * @param <ModelClass>     The model class type
 * @param <FieldType>      The field identifier type
 * @param <RepositoryType> The repository/factory type (can be generic Object if not used)
 */
public class CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> {

    private final DataStoreConfig.DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> builder;

    private CustomDataStoreConfig() {
        this.builder = DataStoreConfig.<ModelClass, FieldType, RepositoryType>builder();
    }

    public static <ModelClass, FieldType, RepositoryType> CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> builder() {
        return new CustomDataStoreConfig<>();
    }

    public CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreInstance(VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        builder.dataStoreInstance(dataStore);
        return this;
    }

    public CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> fields(Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields) {
        builder.fields(fields);
        return this;
    }

    public CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> factory(RepositoryType factory) {
        builder.factory(factory);
        return this;
    }

    public CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> withHooks(DataStoreHooks<ModelClass> hooks) {
        builder.hooks(hooks);
        return this;
    }

    public DataStoreConfig<ModelClass, FieldType, RepositoryType> build() {
        return builder.build();
    }
}
