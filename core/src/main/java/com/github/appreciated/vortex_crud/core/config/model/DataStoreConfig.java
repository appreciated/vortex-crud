package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStoreConfig<DataStoreId, FieldId, KeyType> {

    private KeyType factory;

    private Map<FieldId, Field<DataStoreId, FieldId, KeyType>> fields;

    public DataStoreConfig(KeyType factory) {
        this.factory = factory;
    }

    public KeyType getFactory() {
        return factory;
    }

    public void setFactory(KeyType factory) {
        this.factory = factory;
    }

    public Map<FieldId, Field<DataStoreId, FieldId, KeyType>> getFields() {
        return fields;
    }

    public void setFields(Map<FieldId, Field<DataStoreId, FieldId, KeyType>> fields) {
        this.fields = fields;
    }

    public static class Builder<DataStoreId, FieldId, KeyType> {

        private final DataStoreConfig<DataStoreId, FieldId, KeyType> product;

        public Builder(DataStoreConfig<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withFields(Map<FieldId, Field<DataStoreId, FieldId, KeyType>> fields) {
            product.fields = fields;
            return this;
        }

        public DataStoreConfig<DataStoreId, FieldId, KeyType> build() {
            return product;
        }
    }
}
