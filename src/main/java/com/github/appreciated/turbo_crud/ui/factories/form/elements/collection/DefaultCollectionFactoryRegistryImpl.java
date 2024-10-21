package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link FieldConfig},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultCollectionFactoryRegistryImpl implements TurboCrudCollectionFactoryRegistry {

    private final Map<String, TurboCrudCollectionFactory> factories = new HashMap<>();

    public DefaultCollectionFactoryRegistryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry, TurboCrudDialogFactoryRegistry dialogFactoryRegistry) {
        factories.put("list", new DefaultCollectionFactoryImpl(entityManagerFactoryRegistry, dialogFactoryRegistry));
    }

    public Map<String, TurboCrudCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudCollectionFactory getFactory(String factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory with key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), factory)));
    }

    @Override
    public void addFactory(String key, TurboCrudCollectionFactory factory) {
        factories.put(key, factory);
    }
}