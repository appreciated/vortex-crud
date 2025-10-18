package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.CheckboxFieldFactory;

/**
 * Thin Field type for CheckboxFieldFactory.
 */
public class CheckboxField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {

    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;

    public CheckboxField() {
        this.factory = CheckboxFieldFactory.class;
    }

    public CheckboxField(boolean required, Validation validation) {
        this();
        this.validation = validation;
        this.required = required;
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
        return validation;
    }

    @Override
    public boolean isRequired() {
        return required;
    }
}