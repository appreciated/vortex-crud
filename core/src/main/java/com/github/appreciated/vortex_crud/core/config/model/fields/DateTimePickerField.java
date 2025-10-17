package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

/**
 * Thin Field type for DateTimePickerFactory.
 */
public class DateTimePickerField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
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
