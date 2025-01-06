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

    private final Map<Class<? extends VortexCrudFileProvider>, VortexCrudFileProvider> factories = new HashMap<>();

    public FileProviderRegistry() {
        factories.put(FileProvider.class, new FileProvider());
    }

    public Map<Class<? extends VortexCrudFileProvider>, VortexCrudFileProvider> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudFileProvider getFactory(Class<? extends VortexCrudFileProvider> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudFileProvider> key, VortexCrudFileProvider factory) {
        factories.put(key, factory);
    }
}