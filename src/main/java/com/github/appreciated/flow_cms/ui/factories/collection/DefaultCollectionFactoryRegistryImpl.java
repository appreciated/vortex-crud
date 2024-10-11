package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
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

    public DefaultCollectionFactoryRegistryImpl(FlowCmsConfigService configService) {
        ApplicationConfig configuration = configService.getConfiguration();
        //factories.put("list", new DefaultListElementFactoryImpl(componentFactory, entityManagerService, configService));
    }

    public Map<String, FlowCmsCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public FlowCmsCollectionFactory getFactory(FieldConfig type) {
        return factories.get(type.getType());
    }

    @Override
    public void addFactory(String key, FlowCmsCollectionFactory factory) {
        factories.put(key, factory);
    }
}