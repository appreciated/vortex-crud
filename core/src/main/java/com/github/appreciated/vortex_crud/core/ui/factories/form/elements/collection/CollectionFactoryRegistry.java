package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
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
public class CollectionFactoryRegistry<DataStoreId, FieldId, KeyType> implements VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, KeyType> {

    private final Map<Class<? extends VortexCrudCollectionFactory>, VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> factories = new HashMap<>();

    public CollectionFactoryRegistry(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                                     VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                                     ReflectionService<FieldId> reflectionService,
                                     VortexCrudDataStoreUtilStrategy dataStoreUtil,
                                     ManyToManyPersistenceStrategy<DataStoreId, FieldId, KeyType> manyToManyPersistenceStrategy
    ) {
        factories.put(
                ListCollectionFactory.class,
                new ListCollectionFactory<>(
                        dataStoreFactoryRegistry,
                        dialogFactoryRegistry,
                        reflectionService,
                        dataStoreUtil,
                        manyToManyPersistenceStrategy
                )
        );
    }

    public Map<Class<? extends VortexCrudCollectionFactory>, VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType> getFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> key, VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType> factory) {
        factories.put(key, factory);
    }
}

