package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;

/**
 * Thin Field type for IdFieldFactory.
 */
public class IdField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {

    private Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory;
    private Validation validation;
    private boolean required;

    public IdField() {
        this.factory = (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) IdFieldFactory.class;
        this.validation = new Validation(false);
        this.required = false;
    }

    public IdField(boolean required) {
        this.factory = (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) IdFieldFactory.class;
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