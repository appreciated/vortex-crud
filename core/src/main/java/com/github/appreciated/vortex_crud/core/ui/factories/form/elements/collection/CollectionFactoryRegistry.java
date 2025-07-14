package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link VortexCrudCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class CollectionFactoryRegistry<DataStoreId, FieldId> implements VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId> {

    private final Map<Class<? extends VortexCrudCollectionFactory>, VortexCrudCollectionFactory<DataStoreId, FieldId>> factories = new HashMap<>();

    public CollectionFactoryRegistry(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                     VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                                     VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                     ReflectionService<FieldId> reflectionService) {
        factories.put(ListCollectionFactory.class, new ListCollectionFactory<>(dataStoreFactoryRegistry, dialogFactoryRegistry, resolver, reflectionService));
    }

    public Map<Class<? extends VortexCrudCollectionFactory>, VortexCrudCollectionFactory<DataStoreId, FieldId>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudCollectionFactory<DataStoreId, FieldId> getFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId>> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId>> key, VortexCrudCollectionFactory<DataStoreId, FieldId> factory) {
        factories.put(key, factory);
    }
}

