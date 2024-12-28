package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStoreConfig<FieldId> {

    private Class<? extends TurboCrudDataStore> factory;

    private Map<FieldId, Field> fields;

    public DataStoreConfig(Class<? extends TurboCrudDataStore> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudDataStore> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudDataStore> factory) {
        this.factory = factory;
    }

    public Map<FieldId, Field> getFields() {
        return fields;
    }

    public void setFields(Map<FieldId, Field> fields) {
        this.fields = fields;
    }

    public static class Builder<FieldId> {

        private DataStoreConfig<FieldId> product;

        public Builder(DataStoreConfig<FieldId> product) {
            this.product = product;
        }

        public Builder<FieldId> withFields(Map<FieldId, Field> fields) {
            product.fields = fields;
            return this;
        }

        public DataStoreConfig<FieldId> build() {
            return product;
        }
    }
}
