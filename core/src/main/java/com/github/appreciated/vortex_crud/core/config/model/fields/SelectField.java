package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.SelectFieldFactory;

public class SelectField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final String values;
    private Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory;
    private Validation validation;
    private boolean required;

    public SelectField(String values) {
        this.values = values;
        this.factory = (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) SelectFieldFactory.class;
    }

    public String getValues() {
        return values;
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