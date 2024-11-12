package com.github.appreciated.turbo_crud.file_provider;

import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudFileProviderRegistry} interface.
 * This factory provides components based on the configuration specified in {@link RepositoryField}.
 */

@Service
public class DefaultFileProviderRegistryImpl implements TurboCrudFileProviderRegistry {

    private final Map<String, TurboCrudFileProvider> factories = new HashMap<>();

    public DefaultFileProviderRegistryImpl() {
        factories.put("default", new DefaultFileProviderImpl());
    }

    public Map<String, TurboCrudFileProvider> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudFileProvider getFactory(String type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), type)));
    }

    @Override
    public void addFactory(String key, TurboCrudFileProvider factory) {
        factories.put(key, factory);
    }
}