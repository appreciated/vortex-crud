package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection<DataStoreId, FieldId, ModelClass> {

    private CollectionConfig config;

    public Collection(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>> factory) {
        this.factory = factory;
    }

    private String label;

    private Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>> factory;

    private CollectionConfiguration<DataStoreId, FieldId, ModelClass> data;

    private String emptyMessage;

    private RouteRenderer<DataStoreId, FieldId, ModelClass> child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>> factory) {
        this.factory = factory;
    }

    public CollectionConfiguration<DataStoreId, FieldId, ModelClass> getData() {
        return data;
    }

    public void setData(CollectionConfiguration<DataStoreId, FieldId, ModelClass> data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public RouteRenderer<DataStoreId, FieldId, ModelClass> getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(RouteRenderer<DataStoreId, FieldId, ModelClass> child) {
        this.child = child;
    }

    public abstract static class Builder<DataStoreId, FieldId, ModelClass> {

        private final Collection<DataStoreId, FieldId, ModelClass> product;

        protected Builder(Collection<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withData(CollectionConfiguration<DataStoreId, FieldId, ModelClass> data) {
            product.data = data;
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withChild(RouteRenderer<DataStoreId, FieldId, ModelClass> child) {
            product.child = child;
            return this;
        }

        public Collection<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withFactory(Class<? extends VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>> connect) {
            product.factory = connect;
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
