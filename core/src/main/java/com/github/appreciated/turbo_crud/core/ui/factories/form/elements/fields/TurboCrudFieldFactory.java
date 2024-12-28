package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface TurboCrudFieldFactory {
    Component createComponent(Object table, String field, Field dataStoreField);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
