package com.github.appreciated.turbo_crud.ui.factories.elements.fields;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.vaadin.flow.component.Component;

public interface TurboCrudFieldFactory {
    Component createComponent(String table,String field, FieldConfig fieldConfig);
}
