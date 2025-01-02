package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection<DataStoreId, FieldId> {

    private CollectionConfig config;

    public Collection(Class<? extends TurboCrudDialogFactory<DataStoreId, FieldId>> factory) {
        this.factory = factory;
    }

    private String label;

    private Class<? extends TurboCrudDialogFactory<DataStoreId, FieldId>> factory;

    private CollectionData<DataStoreId, FieldId> data;

    private String emptyMessage;

    private Route<DataStoreId, FieldId> child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<? extends TurboCrudDialogFactory<DataStoreId, FieldId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudDialogFactory<DataStoreId, FieldId>> factory) {
        this.factory = factory;
    }

    public CollectionData<DataStoreId, FieldId> getData() {
        return data;
    }

    public void setData(CollectionData<DataStoreId, FieldId> data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public Route<DataStoreId, FieldId> getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(Route<DataStoreId, FieldId> child) {
        this.child = child;
    }

    public static class Builder<DataStoreId, FieldId> {

        private final Collection<DataStoreId, FieldId> product;

        private Builder(Collection<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder <DataStoreId, FieldId> of(Class<? extends TurboCrudDialogFactory> factory) {
            return new Builder<>(new Collection<>((Class<? extends TurboCrudDialogFactory<DataStoreId,FieldId>>)factory));
        }

        public Builder<DataStoreId,FieldId> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId,FieldId> withData(CollectionData<DataStoreId, FieldId> data) {
            product.data = data;
            return this;
        }

        public Builder<DataStoreId,FieldId> withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<DataStoreId,FieldId> withChild(Route<DataStoreId, FieldId> child) {
            product.child = child;
            return this;
        }

        public Collection<DataStoreId, FieldId> build() {
            return product;
        }

        public Builder<DataStoreId,FieldId> withFactory(Class<? extends TurboCrudDialogFactory<DataStoreId, FieldId>> connect) {
            product.factory = connect;
            return this;
        }

        public Builder<DataStoreId,FieldId> withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
