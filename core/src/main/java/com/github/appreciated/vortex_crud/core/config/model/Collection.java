package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection<DataStoreId, FieldId> {

    private CollectionConfig config;

    public Collection(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId>> factory) {
        this.factory = factory;
    }

    private String label;

    private Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId>> factory;

    private CollectionConfiguration<DataStoreId, FieldId> data;

    private String emptyMessage;

    private RouteRenderer<DataStoreId, FieldId> child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId>> factory) {
        this.factory = factory;
    }

    public CollectionConfiguration<DataStoreId, FieldId> getData() {
        return data;
    }

    public void setData(CollectionConfiguration<DataStoreId, FieldId> data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public RouteRenderer<DataStoreId, FieldId> getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(RouteRenderer<DataStoreId, FieldId> child) {
        this.child = child;
    }

    public static class Builder<DataStoreId, FieldId> {

        private final Collection<DataStoreId, FieldId> product;

        private Builder(Collection<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder <DataStoreId, FieldId> of(Class<? extends VortexCrudDialogFactory> factory) {
            return new Builder<>(new Collection<>((Class<? extends VortexCrudDialogFactory<DataStoreId,FieldId>>)factory));
        }

        public Builder<DataStoreId,FieldId> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId,FieldId> withData(CollectionConfiguration<DataStoreId, FieldId> data) {
            product.data = data;
            return this;
        }

        public Builder<DataStoreId,FieldId> withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<DataStoreId,FieldId> withChild(RouteRenderer<DataStoreId, FieldId> child) {
            product.child = child;
            return this;
        }

        public Collection<DataStoreId, FieldId> build() {
            return product;
        }

        public Builder<DataStoreId,FieldId> withFactory(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId>> connect) {
            product.factory = connect;
            return this;
        }

        public Builder<DataStoreId,FieldId> withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
