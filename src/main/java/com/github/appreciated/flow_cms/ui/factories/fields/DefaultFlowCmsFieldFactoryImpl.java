package com.github.appreciated.flow_cms.ui.factories.fields;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultComboBoxFunction;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultDatePickerFunction;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultTextAreaFunction;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultTextFieldFunction;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultFlowCmsFieldFactoryImpl implements FlowCmsFieldFactory {

    private final Map<String, FlowCmsFieldFunction> componentMap = new HashMap<>();

    public DefaultFlowCmsFieldFactoryImpl(FlowCmsConfigService flowCmsConfigService) {
        ApplicationConfig configuration = flowCmsConfigService.getConfiguration();
        componentMap.put("text", new DefaultTextFieldFunction());
        componentMap.put("textarea", new DefaultTextAreaFunction());
        componentMap.put("date", new DefaultDatePickerFunction());
        componentMap.put("select", new DefaultComboBoxFunction(configuration.getSelects(), configuration.getTablesConfig()));
        componentMap.put("dropdown", new DefaultComboBoxFunction(configuration.getSelects(), configuration.getTablesConfig()));
    }

    public Map<String, FlowCmsFieldFunction> getComponentMap() {
        return componentMap;
    }

    @Override
    public <Comp extends Component & HasValue> Comp createComponent(String table, String field, FieldConfig type) {
        FlowCmsFieldFunction componentSupplier = componentMap.get(type.getType());
        if (componentSupplier != null) {
            return (Comp) componentSupplier.createComponent(table, field, type);
        }
        throw new IllegalArgumentException("Unknown component type: " + type.getType());
    }
}