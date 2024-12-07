package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface TurboCrudFieldFactory {
    Component createComponent(String table, String field, Field repositoryField);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
