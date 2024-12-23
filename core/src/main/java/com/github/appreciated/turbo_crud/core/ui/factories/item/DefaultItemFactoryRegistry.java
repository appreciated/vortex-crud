package com.github.appreciated.turbo_crud.core.ui.factories.item;

import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemFactoryRegistry implements TurboCrudItemFactoryRegistry {

    private final HashMap<Class<? extends TurboCrudItemFactory>, TurboCrudItemFactory> factories = new HashMap<>();

    public DefaultItemFactoryRegistry() {
        factories.put(CardFactory.class, new CardFactory());
    }

    public TurboCrudItemFactory getFactory(Class<? extends TurboCrudItemFactory> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudItemFactory> key, TurboCrudItemFactory factory) {
        factories.put(key, factory);
    }
}