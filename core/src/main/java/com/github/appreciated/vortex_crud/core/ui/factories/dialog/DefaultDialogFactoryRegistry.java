package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
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
public class DefaultDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> implements VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass>  {

    private final Map<Class<?>, VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass> > factories = new HashMap<>();

    public DefaultDialogFactoryRegistry(VortexCrudConfigService<DataStoreId, FieldId, ModelClass>  configService,
                                        VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                                        VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                        VortexCrudForeignKeyResolutionStrategy<FieldId, ModelClass> foreignKeyResolutionStrategy,
                                        ManyToManyPersistenceStrategy<DataStoreId, FieldId, ModelClass> manyToManyPersistenceStrategy
    ) {
        factories.put(FormDialogFactory.class, new FormDialogFactory<>(configService, dataStoreFactoryRegistry, resolver, foreignKeyResolutionStrategy));
        factories.put(FormRouteFactory.class, new FormDialogFactory<>(configService, dataStoreFactoryRegistry, resolver, foreignKeyResolutionStrategy));
        factories.put(ConnectDialogFactory.class, new ConnectDialogFactory<>(dataStoreFactoryRegistry, resolver, manyToManyPersistenceStrategy));
    }

    public Map<Class<?>, VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass> > getFactories() {
        return factories;
    }

    @Override
    public VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>  getFactory(Class<?> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<?> key, VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>  factory) {
        factories.put(key, factory);
    }
}
