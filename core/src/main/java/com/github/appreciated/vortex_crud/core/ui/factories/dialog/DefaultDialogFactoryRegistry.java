package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link VortexCrudDialogFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field}.
 */

@Service
public class DefaultDialogFactoryRegistry<DataStoreId, FieldId> implements VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> {

    private final Map<Class<?>, VortexCrudDialogFactory<DataStoreId, FieldId>> factories = new HashMap<>();

    public DefaultDialogFactoryRegistry(VortexCrudConfigService<DataStoreId, FieldId> configService,
                                        VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                        VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                        VortexCrudForeignKeyResolutionStrategy<FieldId> foreignKeyResolutionStrategy,
                                        ManyToManyPersistenceStrategy<DataStoreId, FieldId> manyToManyPersistenceStrategy,
                                        ReflectionService<FieldId> reflectionService
    ) {
        factories.put(FormDialogFactory.class, new FormDialogFactory<>(configService, dataStoreFactoryRegistry, resolver, foreignKeyResolutionStrategy));
        factories.put(FormRouteFactory.class, new FormDialogFactory<>(configService, dataStoreFactoryRegistry, resolver, foreignKeyResolutionStrategy));
        factories.put(ConnectDialogFactory.class, new ConnectDialogFactory<>(dataStoreFactoryRegistry, resolver, manyToManyPersistenceStrategy, reflectionService));
    }

    public Map<Class<?>, VortexCrudDialogFactory<DataStoreId, FieldId>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudDialogFactory<DataStoreId, FieldId> getFactory(Class<?> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<?> key, VortexCrudDialogFactory<DataStoreId, FieldId> factory) {
        factories.put(key, factory);
    }
}
