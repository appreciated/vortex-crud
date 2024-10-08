package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */


@Service
public class DefaultFlowCmsComponentFactoryImpl implements FlowCmsComponentFactory {

    private final Map<String, Supplier<Component>> componentMap = new HashMap<>();

    public DefaultFlowCmsComponentFactoryImpl() {
        componentMap.put("text", TextField::new);
        componentMap.put("textarea", TextArea::new);
        componentMap.put("date", DatePicker::new);
        componentMap.put("dropdown", ComboBox::new);
        componentMap.put("select", ComboBox::new);
    }

    @Override
    public <Comp extends Component & HasValue> Comp createComponent(FieldConfig type) {
        Supplier<Component> componentSupplier = componentMap.get(type.getType());
        if (componentSupplier != null) {
            return (Comp) componentSupplier.get();
        }
        throw new IllegalArgumentException("Unknown component type: " + type.getType());
    }
}