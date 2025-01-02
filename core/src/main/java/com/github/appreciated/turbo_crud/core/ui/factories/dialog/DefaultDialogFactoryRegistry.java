package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
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
public class DefaultDialogFactoryRegistry<DataStoreId, FieldId> implements TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> {

    private final Map<Class<?>, TurboCrudDialogFactory<DataStoreId, FieldId>> factories = new HashMap<>();

    public DefaultDialogFactoryRegistry(TurboCrudConfigService<DataStoreId, FieldId> configService, TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, TurboCrudDataStoreFieldNameResolver<FieldId> resolver) {
        factories.put(FormDialogFactory.class, new FormDialogFactory<>(configService, dataStoreFactoryRegistry, resolver));
        factories.put(FormRouteFactory.class, new FormDialogFactory<>(configService, dataStoreFactoryRegistry, resolver));
        factories.put(ConnectDialogFactory.class, new ConnectDialogFactory<>(dataStoreFactoryRegistry));
    }

    public Map<Class<?>, TurboCrudDialogFactory<DataStoreId, FieldId>> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudDialogFactory<DataStoreId, FieldId> getFactory(Class<?> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<?> key, TurboCrudDialogFactory<DataStoreId, FieldId> factory) {
        factories.put(key, factory);
    }
}