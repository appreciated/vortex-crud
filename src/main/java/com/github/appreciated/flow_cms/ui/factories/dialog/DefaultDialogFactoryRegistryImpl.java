package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.TurboCrudConfigService;
import com.github.appreciated.flow_cms.service.TurboCrudEntityManagerService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link TurboCrudDialogFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link FieldConfig}.
 */

@Service
public class DefaultDialogFactoryRegistryImpl implements TurboCrudDialogFactoryRegistry {

    private final Map<String, TurboCrudDialogFactory> factories = new HashMap<>();

    public DefaultDialogFactoryRegistryImpl(TurboCrudConfigService configService, TurboCrudEntityManagerService entityManagerService ) {
        factories.put("form", new DefaultDialogFactoryImpl(configService, entityManagerService));
    }

    public Map<String, TurboCrudDialogFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudDialogFactory getFactory(String type) {
        return factories.get(type);
    }

    @Override
    public void addFactory(String key, TurboCrudDialogFactory factory) {
        factories.put(key, factory);
    }
}