package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.Collection;
import java.util.List;

public class DoubleNumberFieldFactory<DataStoreId, FieldId, KeyType> implements VortexCrudFieldFactory<DataStoreId, FieldId, KeyType> {

    @Override
    public Component createComponent(KeyType table, FieldId field, Field<DataStoreId, FieldId, KeyType> dataStoreField) {
        return new NumberField();
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("NUMERIC");
    }
}
