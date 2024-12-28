package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection.TurboCrudCollectionFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class InternalFormElement<DataStoreId, FieldId> {

    private FieldId field;

    private Class<? extends TurboCrudCollectionFactory> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    private String label;

    private String type;

    private Integer span = null;

    Collection<DataStoreId, FieldId> configuration;

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

    public Collection<DataStoreId, FieldId> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Collection<DataStoreId, FieldId> configuration) {
        this.configuration = configuration;
    }

    public static class Builder<DataStoreId, FieldId> {

        private InternalFormElement<DataStoreId, FieldId> product;

        public Builder(InternalFormElement<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId,FieldId> of() {
            return new Builder<>(new InternalFormElement<>());
        }

        public Builder<DataStoreId,FieldId> withField(FieldId field) {
            product.field = field;
            return this;
        }

        public Builder<DataStoreId,FieldId> withFactory(Class<? extends TurboCrudCollectionFactory> factory) {
            product.factory = factory;
            return this;
        }

        public Builder<DataStoreId,FieldId> withReadOnly(boolean readOnly) {
            product.readOnly = readOnly;
            return this;
        }

        public Builder<DataStoreId,FieldId> withReadOnlyForRoles(List<String> readOnlyForRoles) {
            product.readOnlyForRoles = readOnlyForRoles;
            return this;
        }

        public Builder<DataStoreId,FieldId> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<DataStoreId,FieldId> withType(String type) {
            product.type = type;
            return this;
        }

        public Builder<DataStoreId,FieldId> withSpan(Integer span) {
            product.span = span;
            return this;
        }

        public Builder<DataStoreId,FieldId> withConfiguration(Collection<DataStoreId, FieldId> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId,FieldId> addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public InternalFormElement<DataStoreId, FieldId> build() {
            return product;
        }

    }
}
