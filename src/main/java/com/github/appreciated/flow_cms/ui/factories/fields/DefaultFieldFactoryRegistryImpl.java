package com.github.appreciated.flow_cms.ui.factories.fields;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultComboBoxFactory;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultDatePickerFactory;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultTextAreaFactory;
import com.github.appreciated.flow_cms.ui.factories.fields.functions.DefaultTextFieldFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultFieldFactoryRegistryImpl implements FlowCmsFieldFactoryRegistry {

    private final Map<String, FlowCmsFieldFactory> factories = new HashMap<>();

    public DefaultFieldFactoryRegistryImpl(FlowCmsConfigService flowCmsConfigService) {
        ApplicationConfig configuration = flowCmsConfigService.getConfiguration();
        factories.put("text", new DefaultTextFieldFactory());
        factories.put("textarea", new DefaultTextAreaFactory());
        factories.put("date", new DefaultDatePickerFactory());
        factories.put("select", new DefaultComboBoxFactory(configuration.getSelects(), configuration.getTablesConfig()));
        factories.put("dropdown", new DefaultComboBoxFactory(configuration.getSelects(), configuration.getTablesConfig()));
    }

    public Map<String, FlowCmsFieldFactory> getFactories() {
        return factories;
    }

    @Override
    public FlowCmsFieldFactory getFactory(FieldConfig type) {
        return factories.get(type.getType());
    }
}