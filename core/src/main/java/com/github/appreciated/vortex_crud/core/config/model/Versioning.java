package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Versioning<RepositoryType> {

    private boolean enabled;

    private List<RepositoryType> dataStores;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<RepositoryType> getDataStores() {
        return dataStores;
    }

    public void setDataStores(List<RepositoryType> dataStores) {
        this.dataStores = dataStores;
    }

    public static abstract class Builder<RepositoryType> {

        private final Versioning<RepositoryType> product;

        protected Builder(Versioning<RepositoryType> product) {
            this.product = product;
        }

        public Builder<RepositoryType> withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder<RepositoryType> withDataStores(List<RepositoryType> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        @SafeVarargs
        public final Builder<RepositoryType> withDataStores(RepositoryType... dataStores) {
            return withDataStores(List.of(dataStores));
        }

        public Builder<RepositoryType> addDataStores(RepositoryType item) {
            product.dataStores.add(item);
            return this;
        }

        public Versioning<RepositoryType> build() {
            return product;
        }
    }
}
