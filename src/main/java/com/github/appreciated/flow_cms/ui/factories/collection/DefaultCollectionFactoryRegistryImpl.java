package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.config.model.FormField;
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
public class DefaultCollectionFactoryRegistryImpl implements FlowCmsCollectionFactoryRegistry {

    private final Map<String, FlowCmsCollectionFactory> factories = new HashMap<>();

    public DefaultCollectionFactoryRegistryImpl(FlowCmsEntityManagerService entityManagerService) {
        factories.put("list", new DefaultCollectionFactoryImpl(entityManagerService));
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