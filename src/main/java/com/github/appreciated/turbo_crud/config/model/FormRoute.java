package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class FormRoute extends Route {

    private String repository;

    private String factory;

    private String title;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Builder extends Route.Builder {

        private FormRoute product;

        private Builder(FormRoute product) {
            super(product);
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new FormRoute());
        }

        public Builder withRepository(String repository) {
            product.repository = repository;
            return this;
        }

        public Builder withFactory(String factory) {
            product.factory = factory;
            return this;
        }

        public Builder withTitle(String title) {
            product.title = title;
            return this;
        }

        public FormRoute build() {
            return product;
        }
    }
}
