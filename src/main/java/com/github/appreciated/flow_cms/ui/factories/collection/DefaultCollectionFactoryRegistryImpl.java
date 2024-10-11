package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.dialog.FlowCmsDialogFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultCollectionFactoryRegistryImpl implements FlowCmsCollectionFactoryRegistry {

    private final Map<String, FlowCmsCollectionFactory> factories = new HashMap<>();

    public DefaultCollectionFactoryRegistryImpl(FlowCmsEntityManagerService entityManagerService, FlowCmsDialogFactoryRegistry dialogFactoryRegistry) {
        factories.put("list", new DefaultCollectionFactoryImpl(entityManagerService, dialogFactoryRegistry));
    }

    public Map<String, FlowCmsCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public FlowCmsCollectionFactory getFactory(FormField type) {
        return factories.get(type.getFactory());
    }

    @Override
    public void addFactory(String key, FlowCmsCollectionFactory factory) {
        factories.put(key, factory);
    }
}