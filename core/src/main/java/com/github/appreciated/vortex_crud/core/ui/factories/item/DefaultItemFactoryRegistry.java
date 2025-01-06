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
public class DefaultItemFactoryRegistry<FieldId> implements VortexCrudItemFactoryRegistry<FieldId> {

    private final HashMap<Class<? extends VortexCrudItemFactory>, VortexCrudItemFactory<FieldId>> factories = new HashMap<>();

    public DefaultItemFactoryRegistry() {
        Class<CardFactory> cardFactoryClass = CardFactory.class;
        factories.put(cardFactoryClass, new CardFactory<>());
    }

    @Override
    public VortexCrudItemFactory<FieldId> getFactory(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudItemFactory<FieldId>> key, VortexCrudItemFactory<FieldId> factory) {
        factories.put(key, factory);
    }
}