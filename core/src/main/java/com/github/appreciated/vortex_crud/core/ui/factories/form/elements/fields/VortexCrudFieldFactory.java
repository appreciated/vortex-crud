package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.model.GenericEntityMapper;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface VortexCrudFieldFactory<DataStoreId, FieldId> {
    Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId> dataStoreField, GenericEntityMapper mapper);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
