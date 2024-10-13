package com.github.appreciated.turbo_crud.ui.factories.fields.functions;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.ui.factories.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextArea;

public class DefaultTextAreaFactory implements TurboCrudFieldFactory {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        return new TextArea();
    }
}
