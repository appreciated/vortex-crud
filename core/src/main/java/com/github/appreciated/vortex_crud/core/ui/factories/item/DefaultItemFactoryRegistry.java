package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemFactoryRegistry<FieldType> implements VortexCrudItemFactoryRegistry<FieldType> {

    private final HashMap<Class<? extends VortexCrudItemFactory>, VortexCrudItemFactory<FieldType>> factories = new HashMap<>();

    public DefaultItemFactoryRegistry() {
        Class<CardFactory> cardFactoryClass = CardFactory.class;
        factories.put(cardFactoryClass, new CardFactory<>());
    }

    @Override
    public VortexCrudItemFactory<FieldType> getFactory(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudItemFactory<FieldType>> key, VortexCrudItemFactory<FieldType> factory) {
        factories.put(key, factory);
    }
}