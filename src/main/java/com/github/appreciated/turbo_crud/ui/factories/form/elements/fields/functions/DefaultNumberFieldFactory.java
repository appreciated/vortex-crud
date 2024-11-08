package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;

public class DefaultNumberFieldFactory implements TurboCrudFieldFactory {

    @Override
    public Component createComponent(String table, String field, RepositoryField repositoryField) {
        return new NumberField();
    }
}
