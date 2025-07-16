package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Versioning<KeyType> {

    private boolean enabled;

    private List<KeyType> dataStores;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<KeyType> getDataStores() {
        return dataStores;
    }

    public void setDataStores(List<KeyType> dataStores) {
        this.dataStores = dataStores;
    }

    public static abstract class Builder<KeyType> {

        private final Versioning<KeyType> product;

        protected Builder(Versioning<KeyType> product) {
            this.product = product;
        }

        public Builder<KeyType> withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder<KeyType> withDataStores(List<KeyType> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        @SafeVarargs
        public final Builder<KeyType> withDataStores(KeyType... dataStores) {
            return withDataStores(List.of(dataStores));
        }

        public Builder<KeyType> addDataStores(KeyType item) {
            product.dataStores.add(item);
            return this;
        }

        public Versioning<KeyType> build() {
            return product;
        }
    }
}
