package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DefaultDatePickerFunction implements  ComponentFunction {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        DatePicker datePicker = new DatePicker();
        return datePicker;
    }
}
