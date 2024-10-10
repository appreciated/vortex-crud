package com.github.appreciated.flow_cms.ui.factories.elements;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.detail.DefaultFormDetailFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the FlowCmsComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultElementFactoryRegistryImpl implements FlowCmsElementFactoryRegistry {

    private final Map<String, FlowCmsElementFactory> factories = new HashMap<>();

    public DefaultElementFactoryRegistryImpl(FlowCmsConfigService configService) {
        ApplicationConfig configuration = configService.getConfiguration();
        //factories.put("list", new DefaultListElementFactoryImpl(componentFactory, entityManagerService, configService));
    }

    public Map<String, FlowCmsElementFactory> getFactories() {
        return factories;
    }

    @Override
    public FlowCmsElementFactory getFactory(FieldConfig type) {
        return factories.get(type.getType());
    }

    @Override
    public void addFactory(String key, FlowCmsElementFactory factory) {
        factories.put(key, factory);
    }
}