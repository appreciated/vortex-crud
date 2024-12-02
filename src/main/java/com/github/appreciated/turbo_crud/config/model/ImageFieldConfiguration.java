package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldConfiguration extends RouteConfiguration {

    private String factory;

    public ImageFieldConfiguration(String factory) {
        super(factory);
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public static class Builder {

        private ImageFieldConfiguration product;

        private Builder(ImageFieldConfiguration product) {
            this.product = product;
        }

        public static Builder of(String factory) {
            return new Builder(new ImageFieldConfiguration(factory));
        }

        public Builder withFactory(String factory) {
            product.factory = factory;
            return this;
        }

        public ImageFieldConfiguration build() {
            return product;
        }
    }
}
