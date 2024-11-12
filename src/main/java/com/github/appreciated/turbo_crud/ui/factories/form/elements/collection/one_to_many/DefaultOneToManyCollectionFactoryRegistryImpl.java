package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.one_to_many;

import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudOneToManyCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link RepositoryField},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultOneToManyCollectionFactoryRegistryImpl implements TurboCrudOneToManyCollectionFactoryRegistry {

    private final Map<String, TurboCrudOneToManyCollectionFactory> factories = new HashMap<>();

    public DefaultOneToManyCollectionFactoryRegistryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry, TurboCrudDialogFactoryRegistry dialogFactoryRegistry) {
        factories.put("list", new DefaultOneToManyCollectionFactoryImpl(entityManagerFactoryRegistry, dialogFactoryRegistry));
    }

    public Map<String, TurboCrudOneToManyCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudOneToManyCollectionFactory getFactory(String factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), factory)));
    }

    @Override
    public void addFactory(String key, TurboCrudOneToManyCollectionFactory factory) {
        factories.put(key, factory);
    }
}