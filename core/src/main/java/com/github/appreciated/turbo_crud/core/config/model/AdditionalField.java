package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class AdditionalField {

    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Builder {

        private AdditionalField product;

        private Builder(AdditionalField product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new AdditionalField());
        }

        public Builder withName(String name) {
            product.name = name;
            return this;
        }

        public Builder withType(String type) {
            product.type = type;
            return this;
        }

        public AdditionalField build() {
            return product;
        }
    }
}
