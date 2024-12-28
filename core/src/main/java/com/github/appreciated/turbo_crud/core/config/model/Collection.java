package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection<DataStoreId> {

    private CollectionConfig config;

    public Collection(Class<? extends TurboCrudDialogFactory> factory) {
        this.factory = factory;
    }

    private String label;

    private Class<? extends TurboCrudDialogFactory> factory;

    private CollectionData<DataStoreId> data;

    private String emptyMessage;

    private Route<DataStoreId> child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<? extends TurboCrudDialogFactory> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudDialogFactory> factory) {
        this.factory = factory;
    }

    public CollectionData<DataStoreId> getData() {
        return data;
    }

    public void setData(CollectionData<DataStoreId> data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public Route<DataStoreId> getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(Route<DataStoreId> child) {
        this.child = child;
    }

    public static class Builder<DataStoreId> {

        private Collection<DataStoreId> product;

        private Builder(Collection<DataStoreId> product) {
            this.product = product;
        }

        public static <DataStoreId> Builder <DataStoreId> of(Class<? extends TurboCrudDialogFactory> factory) {
            return new Builder<>(new Collection<>(factory));
        }

        public Builder<DataStoreId> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId> withData(CollectionData<DataStoreId> data) {
            product.data = data;
            return this;
        }

        public Builder<DataStoreId> withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<DataStoreId> withChild(Route<DataStoreId> child) {
            product.child = child;
            return this;
        }

        public Collection<DataStoreId> build() {
            return product;
        }

        public Builder<DataStoreId> withFactory(Class<? extends TurboCrudDialogFactory<DataStoreId>> connect) {
            product.factory = connect;
            return this;
        }

        public Builder<DataStoreId> withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
