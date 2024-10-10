package com.github.appreciated.flow_cms.ui.factories.elements;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;

public interface FlowCmsElementFactory {
    Component createElement(String table, String field, FieldConfig fieldConfig);
}
