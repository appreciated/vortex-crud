package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Field<DataStoreId, FieldId> {

    private Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory;

    private boolean primary;

    private boolean required;

    private Validation validation;

    private String defaultValue;

    private String values;

    private DataStoreId dataStore;

    private FieldId field;

    private FieldId filterField;

    private List<String> children;

    private List<String> readOnlyForRoles;

    RouteConfiguration<DataStoreId, FieldId> configuration;

    public Field(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("The factory must not be null");
        }
        this.factory = factory;
    }

    public Field(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, boolean primary) {
        this(factory);
        this.primary = primary;
    }

    public Field(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, String values) {
        this(factory);
        this.values = values;
    }

    public Field(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, boolean primary, boolean required) {
        this(factory, primary);
        this.required = required;
    }

    public Field(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, boolean primary, boolean required, Validation validation) {
        this(factory, primary, required);
        this.validation = validation;
    }

    public Field(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, FieldId field, FieldId filterField, DataStoreId dataStore, List<String> children) {
        this(factory);
        this.field = field;
        this.filterField = filterField;
        this.dataStore = dataStore;
        this.children = children;
    }

    public Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory) {
        this.factory = factory;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public DataStoreId getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStoreId dataStore) {
        this.dataStore = dataStore;
    }

    public FieldId getField() {
        return field;
    }

    public void setField(FieldId field) {
        this.field = field;
    }

    public FieldId getFilterField() {
        return filterField;
    }

    public void setFilterField(FieldId filterField) {
        this.filterField = filterField;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public RouteConfiguration<DataStoreId, FieldId> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteConfiguration<DataStoreId, FieldId> configuration) {
        this.configuration = configuration;
    }

    public static class Builder<DataStoreId, FieldId> {

        private final Field<DataStoreId, FieldId> product;

        public Builder(Field<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory) {
            return new Builder<>(new Field<>(factory));
        }

        public Builder<DataStoreId, FieldId> withPrimary(boolean primary) {
            product.primary = primary;
            return this;
        }

        public Builder<DataStoreId, FieldId> withRequired(boolean required) {
            product.required = required;
            return this;
        }

        public Builder<DataStoreId, FieldId> withValidation(Validation validation) {
            product.validation = validation;
            return this;
        }

        public Builder<DataStoreId, FieldId> withDefaultValue(String defaultValue) {
            product.defaultValue = defaultValue;
            return this;
        }

        public Builder<DataStoreId, FieldId> withValues(String values) {
            product.values = values;
            return this;
        }

        public Builder<DataStoreId, FieldId> withDataStore(DataStoreId dataStore) {
            product.dataStore = dataStore;
            return this;
        }

        public Builder<DataStoreId, FieldId> withField(FieldId field) {
            product.field = field;
            return this;
        }

        public Builder<DataStoreId, FieldId> withFilterField(FieldId filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChildren(List<String> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId, FieldId> withReadOnlyForRoles(String... readOnlyForRoles) {
            product.readOnlyForRoles = List.of(readOnlyForRoles);
            return this;
        }

        public Builder<DataStoreId, FieldId> withConfiguration(RouteConfiguration<DataStoreId, FieldId> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId, FieldId> addChildren(String item) {
            product.children.add(item);
            return this;
        }

        public Builder<DataStoreId, FieldId> addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public Builder<DataStoreId, FieldId> add(boolean primary) {
            return withPrimary(primary);
        }

        public Builder<DataStoreId, FieldId> add(boolean primary, boolean required) {
            return withPrimary(primary).withRequired(required);
        }

        public Field<DataStoreId, FieldId> build() {
            return product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, boolean primary) {
            return new Builder<>(new Field<>(factory, primary));
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, String values) {
            return new Builder<>(new Field<>(factory, values));
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, boolean primary, boolean required) {
            return new Builder<>(new Field<>(factory, primary, required));
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, boolean primary, boolean required, Validation validation) {
            return new Builder<>(new Field<>(factory, primary, required, validation));
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudFieldFactory<DataStoreId, FieldId>> factory, FieldId field, FieldId filterField, DataStoreId dataStore, List<String> children) {
            return new Builder<>(new Field<>(factory, field, filterField, dataStore, children));
        }
    }
}
