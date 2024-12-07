package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStore {

    private Class<? extends TurboCrudDataStore> factory;

    private Map<String, Field> fields;

    public DataStore(Class<? extends TurboCrudDataStore> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudDataStore> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudDataStore> factory) {
        this.factory = factory;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public void setFields(Map<String, Field> fields) {
        this.fields = fields;
    }

    public static class Builder {

        private DataStore product;

        private Builder(DataStore product) {
            this.product = product;
        }

        public static Builder of(Class<? extends TurboCrudDataStore> factory) {
            return new Builder(new DataStore(factory));
        }

        public Builder withFields(Map<String, Field> fields) {
            product.fields = fields;
            return this;
        }

        public DataStore build() {
            return product;
        }
    }
}
