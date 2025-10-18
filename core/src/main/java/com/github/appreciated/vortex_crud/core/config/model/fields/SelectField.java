package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.SelectFieldFactory;

public class SelectField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final String values;
    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;

    public SelectField(String values) {
        this.values = values;
        this.factory = SelectFieldFactory.class;
    }

    public SelectField(String values, boolean required, Validation validation) {
        this(values);
        this.validation = validation;
        this.required = required;
    }

    public String getValues() {
        return values;
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