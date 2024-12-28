package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection.TurboCrudCollectionFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class InternalFormElement<DataStoreId> {

    private String field;

    private Class<? extends TurboCrudCollectionFactory> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    private String label;

    private String type;

    private Integer span = null;

    Collection<DataStoreId> configuration;

    public InternalFormElement() {
    }

    public InternalFormElement(String field, String type, String label) {
        this.field = field;
        this.type = type;
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Class<? extends TurboCrudCollectionFactory> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudCollectionFactory> factory) {
        this.factory = factory;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<String> getReadOnlyForRoles() {
        return readOnlyForRoles;
    }

    public void setReadOnlyForRoles(List<String> readOnlyForRoles) {
        this.readOnlyForRoles = readOnlyForRoles;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public Collection<DataStoreId> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Collection<DataStoreId> configuration) {
        this.configuration = configuration;
    }

    public static class Builder<DataStoreId> {

        private InternalFormElement<DataStoreId> product;

        public Builder(InternalFormElement<DataStoreId> product) {
            this.product = product;
        }

        public static <DataStoreId> Builder<DataStoreId> of() {
            return new Builder<>(new InternalFormElement<>());
        }

        public Builder<DataStoreId>withField(String field) {
            product.field = field;
            return this;
        }

        public Builder<DataStoreId> withFactory(Class<? extends TurboCrudCollectionFactory> factory) {
            product.factory = factory;
            return this;
        }

        public Builder<DataStoreId> withReadOnly(boolean readOnly) {
            product.readOnly = readOnly;
            return this;
        }

        public Builder<DataStoreId> withReadOnlyForRoles(List<String> readOnlyForRoles) {
            product.readOnlyForRoles = readOnlyForRoles;
            return this;
        }

        public Builder<DataStoreId> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId> withType(String type) {
            product.type = type;
            return this;
        }

        public Builder<DataStoreId> withSpan(Integer span) {
            product.span = span;
            return this;
        }

        public Builder<DataStoreId> withConfiguration(Collection configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId> addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public InternalFormElement<DataStoreId> build() {
            return product;
        }

    }
}
