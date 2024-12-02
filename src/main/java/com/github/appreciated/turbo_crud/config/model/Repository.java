package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.Map;

@GenerateBuilder
public class Repository {

    private String factory;

    private Map<String, Field> fields;

    public Repository(String factory) {
        this.factory = factory;
    }

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

        public static Builder of(String factory) {
            return new Builder(new Repository(factory));
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
