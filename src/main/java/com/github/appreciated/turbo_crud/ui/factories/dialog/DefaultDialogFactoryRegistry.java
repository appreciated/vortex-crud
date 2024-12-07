package com.github.appreciated.turbo_crud.ui.factories.dialog;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudDialogFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field}.
 */

@Service
public class DefaultDialogFactoryRegistry implements TurboCrudDialogFactoryRegistry {

    private final Map<Class<? extends TurboCrudDialogFactory>, TurboCrudDialogFactory> factories = new HashMap<>();

    public DefaultDialogFactoryRegistry(TurboCrudConfigService configService, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry) {
        factories.put(TCFormDialogFactory.class, new TCFormDialogFactory(configService, entityManagerFactoryRegistry));
        factories.put(ConnectDialogFactory.class, new ConnectDialogFactory(entityManagerFactoryRegistry));
    }

    public Map<Class<? extends TurboCrudDialogFactory>, TurboCrudDialogFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudDialogFactory getFactory(Class<? extends TurboCrudDialogFactory> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudDialogFactory> key, TurboCrudDialogFactory factory) {
        factories.put(key, factory);
    }
}