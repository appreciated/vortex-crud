package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultDialogFactoryRegistryImpl implements FlowCmsDialogFactoryRegistry {

    private final Map<String, FlowCmsDialogFactory> factories = new HashMap<>();

    public DefaultDialogFactoryRegistryImpl(FlowCmsConfigService configService, FlowCmsEntityManagerService entityManagerService ) {
        factories.put("form", new DefaultDialogFactoryImpl(configService, entityManagerService));
    }

    public Map<String, FlowCmsDialogFactory> getFactories() {
        return factories;
    }

    @Override
    public FlowCmsDialogFactory getFactory(String type) {
        return factories.get(type);
    }

    @Override
    public void addFactory(String key, FlowCmsDialogFactory factory) {
        factories.put(key, factory);
    }
}