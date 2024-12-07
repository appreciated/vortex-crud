package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManager;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.Map;

@GenerateBuilder
public class Repository {

    private Class<? extends TurboCrudEntityManager> factory;

    private Map<String, Field> fields;

    public Repository(Class<? extends TurboCrudEntityManager> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudEntityManager> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudEntityManager> factory) {
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

        public static Builder of(Class<? extends TurboCrudEntityManager> factory) {
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
