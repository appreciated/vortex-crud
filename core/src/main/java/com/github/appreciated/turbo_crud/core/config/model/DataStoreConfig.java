package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class DataStoreConfig<T> {

    private Class<? extends TurboCrudDataStore> factory;

    private Map<T, Field> fields;

    public DataStoreConfig(Class<? extends TurboCrudDataStore> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudDataStore> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudDataStore> factory) {
        this.factory = factory;
    }

    public Map<T, Field> getFields() {
        return fields;
    }

    public void setFields(Map<T, Field> fields) {
        this.fields = fields;
    }

    public static class Builder<T> {

        private DataStoreConfig product;

        public Builder(DataStoreConfig<T> product) {
            this.product = product;
        }

        public  static <T> Builder<T> of(Class<? extends TurboCrudDataStore> factory) {
            return new Builder<>(new DataStoreConfig<>(factory));
        }

        public Builder<T> withFields(Map<T, Field> fields) {
            product.fields = fields;
            return this;
        }

        public DataStoreConfig<T> build() {
            return product;
        }
    }
}
