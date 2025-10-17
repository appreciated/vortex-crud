package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;

/**
 * Thin Field type for TextFieldFactory.
 */
public class TextField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {

    private Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory;
    private Validation validation;
    private boolean required;

    public TextField() {
        this.factory = (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) TextFieldFactory.class;
        this.validation = new Validation(false);
        this.required = false;
    }

    public TextField(boolean required) {
        this.factory = (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) TextFieldFactory.class;
        this.validation = new Validation(required);
        this.required = required;
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