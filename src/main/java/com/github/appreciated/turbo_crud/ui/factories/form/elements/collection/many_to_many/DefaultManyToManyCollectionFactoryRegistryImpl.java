package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.many_to_many;

import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudManyToManyCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link RepositoryField},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultManyToManyCollectionFactoryRegistryImpl implements TurboCrudManyToManyCollectionFactoryRegistry {

    private final Map<String, TurboCrudManyToManyCollectionFactory> factories = new HashMap<>();

    public DefaultManyToManyCollectionFactoryRegistryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry, TurboCrudDialogFactoryRegistry dialogFactoryRegistry) {
        factories.put("list", new DefaultManyToManyCollectionFactoryImpl(entityManagerFactoryRegistry, dialogFactoryRegistry));
    }

    public Map<String, TurboCrudManyToManyCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudManyToManyCollectionFactory getFactory(String factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory with key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), factory)));
    }

    @Override
    public void addFactory(String key, TurboCrudManyToManyCollectionFactory factory) {
        factories.put(key, factory);
    }
}