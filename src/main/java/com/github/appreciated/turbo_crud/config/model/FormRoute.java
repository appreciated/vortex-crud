package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class FormRoute extends Route {

    private String repository;

    private Class<? extends TurboCrudRouteFactory> factory;

    private String title;

    public FormRoute(Class<? extends TurboCrudRouteFactory> factory) {
        super(factory);
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public Class<? extends TurboCrudRouteFactory> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudRouteFactory> factory) {
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

        public static Builder of(Class<? extends TurboCrudRouteFactory> factory) {
            return new Builder(new FormRoute(factory));
        }

        public Builder withConfiguration(RouteConfiguration configuration) {
            product.setConfiguration(configuration);
            return this;
        }

        public Builder withRepository(String repository) {
            product.repository = repository;
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
