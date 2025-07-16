package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface VortexCrudFieldFactory<DataStoreId, FieldId, KeyType> {
    Component createComponent(KeyType table, FieldId field, Field<DataStoreId, FieldId, KeyType> dataStoreField);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
