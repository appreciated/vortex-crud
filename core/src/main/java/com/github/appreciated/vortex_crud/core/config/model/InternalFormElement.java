package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class InternalFormElement<DataStoreId, FieldId, KeyType> {

    private FieldId field;

    private Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    private String label;

    private String type;

    private Integer span = null;

    Collection<DataStoreId, FieldId, KeyType> configuration;

    public InternalFormElement() {
    }

    public InternalFormElement(FieldId field, String type, String label) {
        this.field = field;
        this.type = type;
        this.label = label;
    }

    public FieldId getField() {
        return field;
    }

    public void setField(FieldId field) {
        this.field = field;
    }

    public Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudCollectionFactory> factory) {
        this.factory = (Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>>) factory;
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

    public Collection<DataStoreId, FieldId, KeyType> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Collection<DataStoreId, FieldId, KeyType> configuration) {
        this.configuration = configuration;
    }

    public static class Builder<DataStoreId, FieldId, KeyType> {

        private final InternalFormElement<DataStoreId, FieldId, KeyType> product;

        public Builder(InternalFormElement<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId, KeyType> Builder<DataStoreId, FieldId, KeyType> of() {
            return new Builder<>(new InternalFormElement<>());
        }

        public Builder<DataStoreId, FieldId, KeyType> withField(FieldId field) {
            product.field = field;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withFactory(Class<? extends VortexCrudCollectionFactory> factory) {
            product.factory = (Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>>) factory;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withReadOnly(boolean readOnly) {
            product.readOnly = readOnly;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withReadOnlyForRoles(List<String> readOnlyForRoles) {
            product.readOnlyForRoles = readOnlyForRoles;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withType(String type) {
            product.type = type;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withSpan(Integer span) {
            product.span = span;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withConfiguration(Collection<DataStoreId, FieldId, KeyType> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public InternalFormElement<DataStoreId, FieldId, KeyType> build() {
            return product;
        }

    }
}
