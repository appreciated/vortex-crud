package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Versioning<DataStoreId> {

    private boolean enabled;

    private List<DataStoreId> dataStores;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<DataStoreId> getDataStores() {
        return dataStores;
    }

    public void setDataStores(List<DataStoreId> dataStores) {
        this.dataStores = dataStores;
    }

    public static class Builder<DataStoreId> {

        private final Versioning<DataStoreId> product;

        private Builder(Versioning<DataStoreId> product) {
            this.product = product;
        }

        public static <DataStoreId> Builder<DataStoreId> of() {
            return new Builder<>(new Versioning<>());
        }

        public Builder<DataStoreId> withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder<DataStoreId> withDataStores(List<DataStoreId> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        @SafeVarargs
        public final Builder<DataStoreId> withDataStores(DataStoreId... dataStores) {
            return withDataStores(List.of(dataStores));
        }

        public Builder<DataStoreId> addDataStores(DataStoreId item) {
            product.dataStores.add(item);
            return this;
        }

        public Versioning<DataStoreId> build() {
            return product;
        }
    }
}
