package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.collection.FlowCmsCollectionFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.fields.DefaultFieldFactoryRegistryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity detail renderers.
 * It initializes and provides the appropriate renderer based on the DetailRenderer configuration.
 */

@Service
public class DefaultDetailFactoryRegistryImpl implements FlowCmsDetailFactoryRegistry {

    HashMap<String, FlowCmsDetailFactory> factories = new HashMap<>();

    public DefaultDetailFactoryRegistryImpl(DefaultFieldFactoryRegistryImpl componentFactory, FlowCmsEntityManagerService entityManagerService, FlowCmsConfigService configService,  FlowCmsCollectionFactoryRegistry collectionFactoryRegistry) {
        factories.put("form", new DefaultFormDetailFactoryImpl(componentFactory, entityManagerService, configService, collectionFactoryRegistry));
    }

    public FlowCmsDetailFactory getFactory(String type) {
        return factories.get(type);
    }
}
