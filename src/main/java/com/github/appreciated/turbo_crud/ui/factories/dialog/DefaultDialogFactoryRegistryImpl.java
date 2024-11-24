package com.github.appreciated.turbo_crud.ui.factories.dialog;

import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudDialogFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link RepositoryField}.
 */

@Service
public class DefaultDialogFactoryRegistryImpl implements TurboCrudDialogFactoryRegistry {

    private final Map<String, TurboCrudDialogFactory> factories = new HashMap<>();

    public DefaultDialogFactoryRegistryImpl(TurboCrudConfigService configService, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry) {
        factories.put("form", new DefaultFormDialogFactoryImpl(configService, entityManagerFactoryRegistry));
        factories.put("connect", new DefaultConnectDialogFactoryImpl(entityManagerFactoryRegistry));
    }

    public Map<String, TurboCrudDialogFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudDialogFactory getFactory(String type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), type)));
    }

    @Override
    public void addFactory(String key, TurboCrudDialogFactory factory) {
        factories.put(key, factory);
    }
}