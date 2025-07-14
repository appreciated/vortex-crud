package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface VortexCrudFieldFactory<DataStoreId, FieldId> {
    Component createComponent(Class<? extends DataStoreId> table, FieldId field, Field<DataStoreId, FieldId> dataStoreField);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
