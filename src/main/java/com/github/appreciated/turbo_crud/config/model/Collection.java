package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection {

    private CollectionConfig config;

    public Collection(String factory) {
        this.factory = factory;
    }

    private String label;

    private String factory;

    private CollectionData data;

    private String emptyMessage;

    private Route child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public CollectionData getData() {
        return data;
    }

    public void setData(CollectionData data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public Route getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(Route child) {
        this.child = child;
    }

    public static class Builder {

        private Collection product;

        private Builder(Collection product) {
            this.product = product;
        }

        public static Builder of(String factory) {
            return new Builder(new Collection(factory));
        }

        public Builder withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder withData(CollectionData data) {
            product.data = data;
            return this;
        }

        public Builder withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder withChild(Route child) {
            product.child = child;
            return this;
        }

        public Collection build() {
            return product;
        }

        public Builder withFactory(String connect) {
            product.factory = connect;
            return this;
        }

        public Builder withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
