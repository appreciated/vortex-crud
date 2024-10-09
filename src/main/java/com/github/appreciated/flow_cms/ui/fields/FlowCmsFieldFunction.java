package com.github.appreciated.flow_cms.ui.fields;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;

public interface FlowCmsFieldFunction {
    Component createComponent(String table,String field, FieldConfig fieldConfig);
}
