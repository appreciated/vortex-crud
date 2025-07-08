package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStoreConfig<DataStoreId, FieldId> {

    private Class<? extends VortexCrudDataStore<FieldId, ?>> factory;

    private Map<FieldId, Field<DataStoreId, FieldId>> fields;

    public DataStoreConfig(Class<? extends VortexCrudDataStore<FieldId, ?>> factory) {
        this.factory = factory;
    }

    public Class<? extends VortexCrudDataStore<FieldId, ?>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudDataStore<FieldId, ?>> factory) {
        this.factory = factory;
    }

    public Map<FieldId, Field<DataStoreId, FieldId>> getFields() {
        return fields;
    }

    public void setFields(Map<FieldId, Field<DataStoreId, FieldId>> fields) {
        this.fields = fields;
    }

    public static class Builder<DataStoreId, FieldId> {

        private final DataStoreConfig<DataStoreId, FieldId> product;

        public Builder(DataStoreConfig<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId> withFields(Map<FieldId, Field<DataStoreId, FieldId>> fields) {
            product.fields = fields;
            return this;
        }

        public DataStoreConfig<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
