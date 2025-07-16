package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection<DataStoreId, FieldId, KeyType> {

    private CollectionConfig config;

    public Collection(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    private String label;

    private Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, KeyType>> factory;

    private CollectionConfiguration<DataStoreId, FieldId, KeyType> data;

    private String emptyMessage;

    private RouteRenderer<DataStoreId, FieldId, KeyType> child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    public CollectionConfiguration<DataStoreId, FieldId, KeyType> getData() {
        return data;
    }

    public void setData(CollectionConfiguration<DataStoreId, FieldId, KeyType> data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public RouteRenderer<DataStoreId, FieldId, KeyType> getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(RouteRenderer<DataStoreId, FieldId, KeyType> child) {
        this.child = child;
    }

    public abstract static class Builder<DataStoreId, FieldId, KeyType> {

        private final Collection<DataStoreId, FieldId, KeyType> product;

        protected Builder(Collection<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withData(CollectionConfiguration<DataStoreId, FieldId, KeyType> data) {
            product.data = data;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChild(RouteRenderer<DataStoreId, FieldId, KeyType> child) {
            product.child = child;
            return this;
        }

        public Collection<DataStoreId, FieldId, KeyType> build() {
            return product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withFactory(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, KeyType>> connect) {
            product.factory = connect;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
