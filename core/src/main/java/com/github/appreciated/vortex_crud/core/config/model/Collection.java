package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Collection<ModelClass, FieldType, RepositoryType> {

    private CollectionConfig config;

    public Collection(Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> factory) {
        this.factory = factory;
    }

    private String label;

    private Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> factory;

    private CollectionConfiguration<ModelClass, FieldType, RepositoryType> data;

    private String emptyMessage;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> factory) {
        this.factory = factory;
    }

    public CollectionConfiguration<ModelClass, FieldType, RepositoryType> getData() {
        return data;
    }

    public void setData(CollectionConfiguration<ModelClass, FieldType, RepositoryType> data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> getChild() {
        return child;
    }

    public CollectionConfig getConfig() {
        return config;
    }

    public void setChild(RouteRenderer<ModelClass, FieldType, RepositoryType> child) {
        this.child = child;
    }

    public abstract static class Builder<ModelClass, FieldType, RepositoryType> {

        private final Collection<ModelClass, FieldType, RepositoryType> product;

        protected Builder(Collection<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withData(CollectionConfiguration<ModelClass, FieldType, RepositoryType> data) {
            product.data = data;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withData(CollectionConfiguration.Builder<ModelClass, FieldType, RepositoryType> dataBuilder) {
            product.data = dataBuilder.build();
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withEmptyMessage(String emptyMessage) {
            product.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChild(RouteRenderer<ModelClass, FieldType, RepositoryType> child) {
            product.child = child;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChild(RouteRenderer.Builder<ModelClass, FieldType, RepositoryType> childBuilder) {
            product.child = childBuilder.build();
            return this;
        }

        public Collection<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withFactory(Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> connect) {
            product.factory = connect;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withConfiguration(CollectionConfig title) {
            product.config = title;
            return this;
        }
    }
}
