package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

import java.util.List;

public class ReferenceField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final KeyType dataStore;
    private final FieldId filterField;
    private final List<FieldId> children;

    public ReferenceField(KeyType dataStore, FieldId filterField, List<FieldId> children) {
        this.dataStore = dataStore;
        this.filterField = filterField;
        this.children = children;
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
        return null;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory) {

    }

    @Override
    public Validation getValidation() {
        return null;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
