package com.github.appreciated.vortex_crud.core.file_provider;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link VortexCrudFileProviderRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field}.
 */

@Service
public class FileProviderRegistry implements VortexCrudFileProviderRegistry {

    private final Map<Class<? extends VortexCrudResourceProvider>, VortexCrudResourceProvider> factories = new HashMap<>();

    public FileProviderRegistry() {
        factories.put(ResourceProvider.class, new ResourceProvider());
    }

    public Map<Class<? extends VortexCrudResourceProvider>, VortexCrudResourceProvider> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudResourceProvider getFactory(Class<? extends VortexCrudResourceProvider> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudResourceProvider> key, VortexCrudResourceProvider factory) {
        factories.put(key, factory);
    }
}