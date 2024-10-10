package com.github.appreciated.flow_cms.ui.factories.fields;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextArea;

public class DefaultTextAreaFunction implements FlowCmsFieldFunction {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        TextArea textArea = new TextArea();
        return textArea;
    }
}
