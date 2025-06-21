package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@GenerateBuilder
public class Selects {

    private Map<String, LinkedHashMap<?, String>> configs;

    public Map<String, LinkedHashMap<?, String>> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, LinkedHashMap<?, String>> configs) {
        this.configs = configs;
    }

    public static class Builder {

        private final Selects product;

        private Builder(Selects product) {
            this.product = product;
        }

        public Builder withConfigs(Map<String, LinkedHashMap<?, String>> configs) {
            product.configs = configs;
            return this;
        }

        public Selects build() {
            return product;
        }
    }

    public static Builder of() {
        return new Builder(new Selects());
    }
}
