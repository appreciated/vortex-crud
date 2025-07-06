package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Field<DataStoreId, FieldId, ModelClass> {

    private Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory;

    private boolean primary;

    private boolean required;

    private Validation validation;

    private String defaultValue;

    private String values;

    private DataStoreId dataStore;

    private FieldId field;

    private FieldId filterField;

    private List<FieldId> children;

    private List<String> readOnlyForRoles;

    RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> configuration;

    public Field(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("The factory must not be null");
        }
        this.factory = factory;
    }

    public Field(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, boolean primary) {
        this(factory);
        this.primary = primary;
    }

    public Field(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, String values) {
        this(factory);
        this.values = values;
    }

    public Field(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, boolean primary, boolean required) {
        this(factory, primary);
        this.required = required;
    }

    public Field(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, boolean primary, boolean required, Validation validation) {
        this(factory, primary, required);
        this.validation = validation;
    }

    public Field(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, FieldId field, FieldId filterField, DataStoreId dataStore, List<FieldId> children) {
        this(factory);
        this.field = field;
        this.filterField = filterField;
        this.dataStore = dataStore;
        this.children = children;
    }

    public Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory) {
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

    public List<FieldId> getChildren() {
        return children;
    }

    public void setChildren(List<FieldId> children) {
        this.children = children;
    }

    public RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> configuration) {
        this.configuration = configuration;
    }

    public static class Builder<DataStoreId, FieldId, ModelClass> {

        private final Field<DataStoreId, FieldId, ModelClass> product;

        public Builder(Field<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId, ModelClass> Builder<DataStoreId, FieldId, ModelClass> of(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory) {
            return new Builder<>(new Field<>(factory));
        }

        public Builder<DataStoreId, FieldId, ModelClass> withPrimary(boolean primary) {
            product.primary = primary;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withRequired(boolean required) {
            product.required = required;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withValidation(Validation validation) {
            product.validation = validation;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withDefaultValue(String defaultValue) {
            product.defaultValue = defaultValue;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withValues(String values) {
            product.values = values;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withDataStore(DataStoreId dataStore) {
            product.dataStore = dataStore;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withField(FieldId field) {
            product.field = field;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withFilterField(FieldId filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChildren(List<FieldId> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withReadOnlyForRoles(String... readOnlyForRoles) {
            product.readOnlyForRoles = List.of(readOnlyForRoles);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withConfiguration(RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> addChildren(FieldId item) {
            product.children.add(item);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> add(boolean primary) {
            return withPrimary(primary);
        }

        public Builder<DataStoreId, FieldId, ModelClass> add(boolean primary, boolean required) {
            return withPrimary(primary).withRequired(required);
        }

        public Field<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }

        public static <DataStoreId, FieldId, ModelClass> Builder<DataStoreId, FieldId, ModelClass> of(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, boolean primary) {
            return new Builder<>(new Field<>(factory, primary));
        }

        public static <DataStoreId, FieldId, ModelClass> Builder<DataStoreId, FieldId, ModelClass> of(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, String values) {
            return new Builder<>(new Field<>(factory, values));
        }

        public static <DataStoreId, FieldId, ModelClass> Builder<DataStoreId, FieldId, ModelClass> of(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, boolean primary, boolean required) {
            return new Builder<>(new Field<>(factory, primary, required));
        }

        public static <DataStoreId, FieldId, ModelClass> Builder<DataStoreId, FieldId, ModelClass> of(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, boolean primary, boolean required, Validation validation) {
            return new Builder<>(new Field<>(factory, primary, required, validation));
        }

        public static <DataStoreId, FieldId, ModelClass> Builder<DataStoreId, FieldId, ModelClass> of(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factory, FieldId field, FieldId filterField, DataStoreId dataStore, List<FieldId> children) {
            return new Builder<>(new Field<>(factory, field, filterField, dataStore, children));
        }
    }
}
