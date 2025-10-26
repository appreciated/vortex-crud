package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;

import java.util.List;

public class ReferenceField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    private final RepositoryType dataStore;
    private final FieldType field;
    private final FieldType filterField;
    private final List<FieldType> children;
    private boolean required;
    private Class<? extends VortexCrudFieldFactory> factory;
    private List<String> writeRoles;
    private List<String> readOnlyRoles;

    //of(TableField<?, ?> field, TableField<?, ?> filterField, TableImpl<?> dataStore, List<TableField<?, ?>> children) {

    public ReferenceField(RepositoryType dataStore, FieldType field, FieldType filterField, List<FieldType> children) {
        this(dataStore, field, filterField, children, false);
    }

    public ReferenceField(RepositoryType dataStore, FieldType field, FieldType filterField, List<FieldType> children, boolean required) {
        this.dataStore = dataStore;
        this.field = field;
        this.filterField = filterField;
        this.children = children;
        this.required = required;
        this.factory = ReferenceFieldFactory.class;
    }

    public ReferenceField(RepositoryType dataStore, FieldType filterField, List<FieldType> children) {
        this(dataStore, null, filterField, children, false);
    }

    public ReferenceField(RepositoryType dataStore, FieldType filterField, List<FieldType> children, boolean required) {
        this(dataStore, null, filterField, children, required);
    }

    public RepositoryType getDataStore() {
        return dataStore;
    }

    public FieldType getFilterField() {
        return filterField;
    }

    public List<FieldType> getChildren() {
        return children;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory) {
        this.factory = factory;
    }

    @Override
    public Validation getValidation() {
        return null;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    public FieldType getField() {
        return field;
    }

    @Override
    public void setWriteRoles(List<String> writeRoles) {
        this.writeRoles = writeRoles;
    }

    @Override
    public List<String> getWriteRoles() {
        return writeRoles;
    }

    @Override
    public void setReadOnlyRoles(List<String> readOnlyRoles) {
        this.readOnlyRoles = readOnlyRoles;
    }

    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }
}