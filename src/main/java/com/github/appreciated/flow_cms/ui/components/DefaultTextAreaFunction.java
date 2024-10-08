package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DefaultTextAreaFunction implements  ComponentFunction {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        TextArea textArea = new TextArea();
        return textArea;
    }
}
