package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.form.FormRouteFactory;
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

    private final Map<Class<?>, TurboCrudDialogFactory> factories = new HashMap<>();

    public DefaultDialogFactoryRegistry(TurboCrudConfigService configService, TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry) {
        factories.put(FormDialogFactory.class, new FormDialogFactory(configService, dataStoreFactoryRegistry));
        factories.put(FormRouteFactory.class, new FormDialogFactory(configService, dataStoreFactoryRegistry));
        factories.put(ConnectDialogFactory.class, new ConnectDialogFactory(dataStoreFactoryRegistry));
    }

    public Map<Class<?>, TurboCrudDialogFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudDialogFactory getFactory(Class<?> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<?> key, TurboCrudDialogFactory factory) {
        factories.put(key, factory);
    }
}