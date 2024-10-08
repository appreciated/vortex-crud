package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;

public interface ComponentFunction {
    Component createComponent(String table,String field, FieldConfig fieldConfig);
}
