package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class InternalFormElement<ModelClass, FieldType, RepositoryType> {

    private FieldType field;

    private Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    private String label;

    private ViewFieldType type;

    private int span = 1;

    Collection<ModelClass, FieldType, RepositoryType> configuration;

    public InternalFormElement() {
    }

    public InternalFormElement(FieldType field, ViewFieldType type, String label) {
        this.field = field;
        this.type = type;
        this.label = label;
    }

    public FieldType getField() {
        return field;
    }

    public void setField(FieldType field) {
        this.field = field;
    }

    public Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudCollectionFactory> factory) {
        this.factory = (Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>>) factory;
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

    public ViewFieldType getType() {
        return type;
    }

    public void setType(ViewFieldType type) {
        this.type = type;
    }

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public Collection<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Collection<ModelClass, FieldType, RepositoryType> configuration) {
        this.configuration = configuration;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final InternalFormElement<ModelClass, FieldType, RepositoryType> product;

        public Builder(InternalFormElement<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public static <ModelClass, FieldType, RepositoryType> Builder<ModelClass, FieldType, RepositoryType> of() {
            return new Builder<>(new InternalFormElement<>());
        }

        public Builder<ModelClass, FieldType, RepositoryType> withField(FieldType field) {
            product.field = field;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withFactory(Class<? extends VortexCrudCollectionFactory> factory) {
            product.factory = (Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>>) factory;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withReadOnly(boolean readOnly) {
            product.readOnly = readOnly;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withReadOnlyForRoles(List<String> readOnlyForRoles) {
            product.readOnlyForRoles = readOnlyForRoles;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withType(ViewFieldType type) {
            product.type = type;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withSpan(Integer span) {
            product.span = span;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withConfiguration(Collection<ModelClass, FieldType, RepositoryType> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public InternalFormElement<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }

    }
}
