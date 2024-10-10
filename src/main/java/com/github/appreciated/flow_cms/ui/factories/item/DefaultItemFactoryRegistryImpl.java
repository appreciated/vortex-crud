package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemFactoryRegistryImpl implements FlowCmsItemFactoryRegistry {

    HashMap<String, FlowCmsItemFactory> factories = new HashMap<>();

    public DefaultItemFactoryRegistryImpl() {
        factories.put("card", new DefaultItemCardFactoryImpl());
    }

    public FlowCmsItemFactory getFactory(ItemFactoryConfig routeConfig) {
        return factories.get(routeConfig.getType());
    }
}