package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStoreConfig<DataStoreId, FieldId, ModelClass> {

    private Class<? extends VortexCrudDataStore<FieldId, ModelClass>> factory;

    private Map<FieldId, Field<DataStoreId, FieldId, ModelClass>> fields;

    public DataStoreConfig(Class<? extends VortexCrudDataStore<FieldId, ModelClass>> factory) {
        this.factory = factory;
    }

    public Class<? extends VortexCrudDataStore<FieldId, ModelClass>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudDataStore<FieldId, ModelClass>> factory) {
        this.factory = factory;
    }

    public Map<FieldId, Field<DataStoreId, FieldId, ModelClass>> getFields() {
        return fields;
    }

    public void setFields(Map<FieldId, Field<DataStoreId, FieldId, ModelClass>> fields) {
        this.fields = fields;
    }

    public static class Builder<DataStoreId, FieldId, ModelClass> {

        private final DataStoreConfig<DataStoreId, FieldId, ModelClass> product;

        public Builder(DataStoreConfig<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withFields(Map<FieldId, Field<DataStoreId, FieldId, ModelClass>> fields) {
            product.fields = fields;
            return this;
        }

        public DataStoreConfig<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }
    }
}
