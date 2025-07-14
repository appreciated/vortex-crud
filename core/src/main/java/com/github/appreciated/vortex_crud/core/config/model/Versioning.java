package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Versioning<DataStoreId> {

    private boolean enabled;

    private List<Class<? extends DataStoreId>> dataStores;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Class<? extends DataStoreId>> getDataStores() {
        return dataStores;
    }

    public void setDataStores(List<Class<? extends DataStoreId>> dataStores) {
        this.dataStores = dataStores;
    }

    public static abstract class Builder<DataStoreId> {

        private final Versioning<DataStoreId> product;

        protected Builder(Versioning<DataStoreId> product) {
            this.product = product;
        }

        public Builder<DataStoreId> withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder<DataStoreId> withDataStores(List<Class<? extends DataStoreId>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        @SafeVarargs
        public final Builder<DataStoreId> withDataStores(Class<? extends DataStoreId>... dataStores) {
            return withDataStores(List.of(dataStores));
        }

        public Builder<DataStoreId> addDataStores(Class<? extends DataStoreId> item) {
            product.dataStores.add(item);
            return this;
        }

        public Versioning<DataStoreId> build() {
            return product;
        }
    }
}
