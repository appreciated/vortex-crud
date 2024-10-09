package com.github.appreciated.flow_cms.ui.fields;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;

public class DefaultDatePickerFunction implements FlowCmsFieldFunction {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        DatePicker datePicker = new DatePicker();
        return datePicker;
    }
}
