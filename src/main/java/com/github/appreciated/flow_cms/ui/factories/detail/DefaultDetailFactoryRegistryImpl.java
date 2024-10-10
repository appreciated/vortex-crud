package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
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

    public DefaultDetailFactoryRegistryImpl(DefaultFieldFactoryRegistryImpl componentFactory, FlowCmsEntityManagerService entityManagerService, FlowCmsConfigService configService) {
        factories.put("form", new DefaultFormDetailFactoryImpl(componentFactory, entityManagerService, configService));
    }

    public FlowCmsDetailFactory getFactory(DetailFactory routeConfig) {
        return factories.get(routeConfig.getType());
    }
}


