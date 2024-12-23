package com.github.appreciated.turbo_crud.core.file_provider;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudFileProviderRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field}.
 */

@Service
public class FileProviderRegistry implements TurboCrudFileProviderRegistry {

    private final Map<Class<? extends TurboCrudFileProvider>, TurboCrudFileProvider> factories = new HashMap<>();

    public FileProviderRegistry() {
        factories.put(FileProvider.class, new FileProvider());
    }

    public Map<Class<? extends TurboCrudFileProvider>, TurboCrudFileProvider> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudFileProvider getFactory(Class<? extends TurboCrudFileProvider> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudFileProvider> key, TurboCrudFileProvider factory) {
        factories.put(key, factory);
    }
}