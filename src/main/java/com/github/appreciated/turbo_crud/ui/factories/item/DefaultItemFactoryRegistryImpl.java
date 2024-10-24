package com.github.appreciated.turbo_crud.ui.factories.item;

import com.typesafe.config.Config;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemFactoryRegistryImpl implements TurboCrudItemFactoryRegistry {

    private final HashMap<String, TurboCrudItemFactory> factories = new HashMap<>();

    public DefaultItemFactoryRegistryImpl() {
        factories.put("card", new DefaultItemCardFactoryImpl());
    }

    public TurboCrudItemFactory getFactory(Config routeConfig) {
        return factories.get(routeConfig.getString("factory"));
    }

    @Override
    public void addFactory(String key, TurboCrudItemFactory factory) {
        factories.put(key, factory);
    }
}