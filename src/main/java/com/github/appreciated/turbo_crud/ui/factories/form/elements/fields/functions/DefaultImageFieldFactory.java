package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component.ImageHasValue;
import com.vaadin.flow.component.Component;

public class DefaultImageFieldFactory implements TurboCrudFieldFactory {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        return new ImageHasValue();
    }

}
