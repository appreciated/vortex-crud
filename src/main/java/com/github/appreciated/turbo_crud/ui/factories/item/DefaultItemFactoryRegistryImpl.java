package com.github.appreciated.turbo_crud.ui.factories.item;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemFactoryRegistryImpl implements TurboCrudItemFactoryRegistry {

    private final HashMap<Class<? extends TurboCrudItemFactory>, TurboCrudItemFactory> factories = new HashMap<>();

    public DefaultItemFactoryRegistryImpl() {
        factories.put(TCItemCardFactoryImpl.class, new TCItemCardFactoryImpl());
    }

    public TurboCrudItemFactory getFactory(Class<? extends TurboCrudItemFactory> factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(Class<? extends TurboCrudItemFactory> key, TurboCrudItemFactory factory) {
        factories.put(key, factory);
    }
}