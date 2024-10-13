package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.TurboCrudEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link TurboCrudCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link FieldConfig},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultCollectionFactoryRegistryImpl implements TurboCrudCollectionFactoryRegistry {

    private final Map<String, TurboCrudCollectionFactory> factories = new HashMap<>();

    public DefaultCollectionFactoryRegistryImpl(TurboCrudEntityManagerService entityManagerService, TurboCrudDialogFactoryRegistry dialogFactoryRegistry) {
        factories.put("list", new DefaultCollectionFactoryImpl(entityManagerService, dialogFactoryRegistry));
    }

    public Map<String, TurboCrudCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudCollectionFactory getFactory(FormField type) {
        return factories.get(type.getFactory());
    }

    @Override
    public void addFactory(String key, TurboCrudCollectionFactory factory) {
        factories.put(key, factory);
    }
}