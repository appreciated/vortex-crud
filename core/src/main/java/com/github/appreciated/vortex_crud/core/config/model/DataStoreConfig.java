package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStoreConfig<ModelClass, FieldType, RepositoryType> {

    private RepositoryType factory;

    private Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields;

    public DataStoreConfig(RepositoryType factory) {
        this.factory = factory;
    }

    public RepositoryType getFactory() {
        return factory;
    }

    public void setFactory(RepositoryType factory) {
        this.factory = factory;
    }

    public Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> getFields() {
        return fields;
    }

    public void setFields(Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields) {
        this.fields = fields;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final DataStoreConfig<ModelClass, FieldType, RepositoryType> product;

        public Builder(DataStoreConfig<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withFields(Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields) {
            product.fields = fields;
            return this;
        }

        public DataStoreConfig<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }
}
