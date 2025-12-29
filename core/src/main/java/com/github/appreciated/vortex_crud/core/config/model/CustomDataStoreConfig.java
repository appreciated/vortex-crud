package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.util.Map;

/**
 * A builder for creating a DataStoreConfig with a custom VortexCrudDataStore.
 * This allows flexible configuration without being tied to a specific framework (like jOOQ or JPA).
 *
 * @param <ModelClass>     The type of the model/entity.
 * @param <FieldType>      The type of the field identifier (e.g., String, TableField).
 * @param <RepositoryType> The type of the repository/factory identifier.
 */
public class CustomDataStoreConfig<ModelClass, FieldType, RepositoryType> {

    public static <ModelClass, FieldType, RepositoryType> Builder<ModelClass, FieldType, RepositoryType> builder() {
        return new Builder<>();
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {
        private RepositoryType factory;
        private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;
        private Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields;
        private DataStoreHooks<ModelClass> hooks = new DataStoreHooks<>();

        public Builder<ModelClass, FieldType, RepositoryType> factory(RepositoryType factory) {
            this.factory = factory;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> dataStoreInstance(VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance) {
            this.dataStoreInstance = dataStoreInstance;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> fields(Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields) {
            this.fields = fields;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> hooks(DataStoreHooks<ModelClass> hooks) {
            this.hooks = hooks;
            return this;
        }

        public DataStoreConfig<ModelClass, FieldType, RepositoryType> build() {
            return DataStoreConfig.<ModelClass, FieldType, RepositoryType>builder()
                    .factory(factory)
                    .dataStoreInstance(dataStoreInstance)
                    .fields(fields)
                    .hooks(hooks)
                    .build();
        }
    }
}
