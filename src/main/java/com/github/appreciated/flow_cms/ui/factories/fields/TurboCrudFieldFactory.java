package com.github.appreciated.flow_cms.ui.factories.fields;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;

public interface TurboCrudFieldFactory {
    Component createComponent(String table,String field, FieldConfig fieldConfig);
}
