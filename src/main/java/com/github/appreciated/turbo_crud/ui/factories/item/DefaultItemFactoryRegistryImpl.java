package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactoryConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemFactoryRegistryImpl implements TurboCrudItemFactoryRegistry {

    HashMap<String, TurboCrudItemFactory> factories = new HashMap<>();

    public DefaultItemFactoryRegistryImpl() {
        factories.put("card", new DefaultItemCardFactoryImpl());
    }

    public TurboCrudItemFactory getFactory(ItemFactoryConfig routeConfig) {
        return factories.get(routeConfig.getFactory());
    }

    @Override
    public void addFactory(String key, TurboCrudItemFactory factory) {
        factories.put(key, factory);
    }
}