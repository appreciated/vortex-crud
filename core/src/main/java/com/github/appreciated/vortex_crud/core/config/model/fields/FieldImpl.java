package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

import java.util.List;

/**
 * Minimal implementation to keep backward compatibility with legacy builder API.
 */
public class FieldImpl<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory;
    private Validation validation;
    private boolean required;

    public FieldImpl(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    public FieldImpl(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory, boolean primary) {
        this.factory = factory;
        // primary flag accepted for compatibility; not used here
    }

    public FieldImpl(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory, String values) {
        this.factory = factory;
        // values accepted for compatibility; not used here
    }

    public FieldImpl(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory, boolean primary, boolean required) {
        this.factory = factory;
        this.required = required;
        // primary flag accepted for compatibility; not used here
    }

    public FieldImpl(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory, boolean primary, boolean required, Validation validation) {
        this.factory = factory;
        this.required = required;
        this.validation = validation;
        // primary flag accepted for compatibility; not used here
    }

    public FieldImpl(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory,
                     FieldId field,
                     FieldId filterField,
                     KeyType dataStore,
                     List<FieldId> children) {
        this.factory = factory;
        // parameters accepted for compatibility; not used here
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    @Override
    public Validation getValidation() {
        return validation;
    }

    @Override
    public boolean isRequired() {
        return required;
    }
}
