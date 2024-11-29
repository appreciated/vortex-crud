package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.Map;

@GenerateBuilder
public class Repository {

    private String factory;

    private Map<String, Field> fields;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public void setFields(Map<String, Field> fields) {
        this.fields = fields;
    }

    public static class Builder {

        private Repository product;

        private Builder(Repository product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Repository());
        }

        public Builder withFactory(String factory) {
            product.factory = factory;
            return this;
        }

        public Builder withFields(Map<String, Field> fields) {
            product.fields = fields;
            return this;
        }

        public Repository build() {
            return product;
        }
    }
}
