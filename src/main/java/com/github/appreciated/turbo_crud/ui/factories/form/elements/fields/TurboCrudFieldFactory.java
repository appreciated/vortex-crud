package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.vaadin.flow.component.Component;

public interface TurboCrudFieldFactory {
    Component createComponent(String table, String field, RepositoryField repositoryField);
}
