package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface TurboCrudFieldFactory<DataStoreId, FieldId> {
    Component createComponent(DataStoreId table, FieldId field, Field dataStoreField);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
