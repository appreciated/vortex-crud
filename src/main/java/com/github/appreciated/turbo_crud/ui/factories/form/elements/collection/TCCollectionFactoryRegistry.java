package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class TCCollectionFactoryRegistry implements TurboCrudCollectionFactoryRegistry {

    private final Map<Class<? extends TurboCrudCollectionFactory>, TurboCrudCollectionFactory> factories = new HashMap<>();

    public TCCollectionFactoryRegistry(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry, TurboCrudDialogFactoryRegistry dialogFactoryRegistry) {
        factories.put(TCListCollectionFactory.class, new TCListCollectionFactory(entityManagerFactoryRegistry, dialogFactoryRegistry));
    }

    public Map<Class<? extends TurboCrudCollectionFactory>, TurboCrudCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudCollectionFactory getFactory(Class<? extends TurboCrudCollectionFactory> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudCollectionFactory> key, TurboCrudCollectionFactory factory) {
        factories.put(key, factory);
    }
}

