package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Versioning {

    private boolean enabled;

    private List<String> dataStores;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getDataStores() {
        return dataStores;
    }

    public void setDataStores(List<String> dataStores) {
        this.dataStores = dataStores;
    }

    public static class Builder {

        private Versioning product;

        private Builder(Versioning product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Versioning());
        }

        public Builder withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder withDataStores(List<String> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Builder withDataStores(String... dataStores) {
            return withDataStores(List.of(dataStores));
        }

        public Builder addDataStores(String item) {
            product.dataStores.add(item);
            return this;
        }

        public Versioning build() {
            return product;
        }
    }
}
