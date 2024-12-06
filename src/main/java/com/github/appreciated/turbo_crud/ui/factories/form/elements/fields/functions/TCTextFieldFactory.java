package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

public class TCTextFieldFactory implements TurboCrudFieldFactory {

    @Override
    public Component createComponent(String table, String field, Field repositoryField) {
        return new TextField();
    }
}
