package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */


@Service
public class DefaultFlowCmsComponentFactoryImpl implements FlowCmsComponentFactory {

    private final Map<String,ComponentFunction> componentMap = new HashMap<>();

    public DefaultFlowCmsComponentFactoryImpl(FlowCmsConfigService flowCmsConfigService) {
        ApplicationConfig configuration = flowCmsConfigService.getConfiguration();
        componentMap.put("text", new DefaultTextFieldFunction());
        componentMap.put("textarea", new DefaultTextAreaFunction());
        componentMap.put("date", new DefaultDatePickerFunction());
        componentMap.put("select", new DefaultComboBoxFunction(configuration.getSelects(), configuration.getTablesConfig()));
        componentMap.put("dropdown", new DefaultComboBoxFunction(configuration.getSelects(), configuration.getTablesConfig()));
    }

    @Override
    public <Comp extends Component & HasValue> Comp createComponent(String table, String field, FieldConfig type) {
        ComponentFunction componentSupplier = componentMap.get(type.getType());
        if (componentSupplier != null) {
            return (Comp) componentSupplier.createComponent(table, field, type);
        }
        throw new IllegalArgumentException("Unknown component type: " + type.getType());
    }
}