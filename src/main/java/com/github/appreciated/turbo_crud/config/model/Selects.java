package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.Map;

@GenerateBuilder
public class Selects {

    private Map<String, Map<String, String>> configs;

    public Map<String, Map<String, String>> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, Map<String, String>> configs) {
        this.configs = configs;
    }

    public static class Builder {

        private Selects product;

        private Builder(Selects product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Selects());
        }

        public Builder withConfigs(Map<String, Map<String, String>> configs) {
            product.configs = configs;
            return this;
        }

        public Selects build() {
            return product;
        }
    }
}
