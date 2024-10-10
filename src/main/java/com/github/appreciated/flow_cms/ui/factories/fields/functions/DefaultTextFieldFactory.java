package com.github.appreciated.flow_cms.ui.factories.fields.functions;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.ui.factories.fields.FlowCmsFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

public class DefaultTextFieldFactory implements FlowCmsFieldFactory {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        TextField textField = new TextField();
        return textField;
    }
}
