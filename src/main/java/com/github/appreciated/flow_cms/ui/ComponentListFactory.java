package com.github.appreciated.flow_cms.ui;

import com.github.appreciated.flow_cms.ui.components.CustomComboBox;
import com.github.appreciated.flow_cms.ui.components.CustomDatePicker;
import com.github.appreciated.flow_cms.ui.components.CustomTextArea;
import com.github.appreciated.flow_cms.ui.components.CustomTextField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ComponentListFactory {

    private static final Map<String, Supplier<Component>> componentMap = new HashMap<>();

    static {
        componentMap.put("text", TextField::new);
        componentMap.put("textarea", TextArea::new);
        componentMap.put("date", DatePicker::new);
        componentMap.put("dropdown", ComboBox::new);

        // Register custom components
        componentMap.put("custom_text", CustomTextField::new);
        componentMap.put("custom_textarea", CustomTextArea::new);
        componentMap.put("custom_date", CustomDatePicker::new);
        componentMap.put("custom_dropdown", CustomComboBox::new);
    }

        public static Component createComponent(String type) {
            Supplier<Component> componentSupplier = componentMap.get(type);
            if (componentSupplier != null) {
                return componentSupplier.get();
            }
            throw new IllegalArgumentException("Unknown component type: " +
  type);
        }
    }

