package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;

import java.util.List;

public class ReferenceField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final KeyType dataStore;
    private final FieldId field;
    private final FieldId filterField;
    private final List<FieldId> children;
    private boolean required;
    private Class<? extends VortexCrudFieldFactory> factory;

    //of(TableField<?, ?> field, TableField<?, ?> filterField, TableImpl<?> dataStore, List<TableField<?, ?>> children) {

    public ReferenceField(KeyType dataStore, FieldId field, FieldId filterField, List<FieldId> children) {
        this(dataStore, field, filterField, children, false);
    }

    public ReferenceField(KeyType dataStore, FieldId field, FieldId filterField, List<FieldId> children, boolean required) {
        this.dataStore = dataStore;
        this.field = field;
        this.filterField = filterField;
        this.children = children;
        this.required = required;
        this.factory = ReferenceFieldFactory.class;
    }

    public ReferenceField(KeyType dataStore, FieldId filterField, List<FieldId> children) {
        this(dataStore, null, filterField, children, false);
    }

    public ReferenceField(KeyType dataStore, FieldId filterField, List<FieldId> children, boolean required) {
        this(dataStore, null, filterField, children, required);
    }

    public KeyType getDataStore() {
        return dataStore;
    }

    public FieldId getFilterField() {
        return filterField;
    }

    public List<FieldId> getChildren() {
        return children;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory) {
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

    public FieldId getField() {
        return field;
    }
}