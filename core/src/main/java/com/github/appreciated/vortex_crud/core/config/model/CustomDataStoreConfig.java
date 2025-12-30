package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.Builder;

import java.util.Map;

/**
 * Builder class for configuring custom data stores with flexible types.
 * Allows simplified configuration of data stores that don't need the full query capabilities
 * of standard database-backed stores.
 */
public class CustomDataStoreConfig {

    /**
     * Creates a builder for a custom data store configuration.
     *
     * @param <ModelClass>     The type of the model/entity
     * @param <FieldType>      The type of the field identifier (e.g., String for Maps)
     * @param <RepositoryType> The type of the repository/store backend
     * @return A builder for DataStoreConfig
     */
    public static <ModelClass, FieldType, RepositoryType> DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> builder() {
        return new DataStoreConfigBuilder<>();
    }

    public static class DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> {
        private RepositoryType factory;
        private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;
        private Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields;
        private DataStoreHooks<?> hooks = new DataStoreHooks<>();

        public DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> factory(RepositoryType factory) {
            this.factory = factory;
            return this;
        }

        public DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> dataStoreInstance(VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance) {
            this.dataStoreInstance = dataStoreInstance;
            return this;
        }

        public DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> fields(Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields) {
            this.fields = fields;
            return this;
        }

        public DataStoreConfigBuilder<ModelClass, FieldType, RepositoryType> hooks(DataStoreHooks<?> hooks) {
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
