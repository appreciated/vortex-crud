package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
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
public class CollectionFactoryRegistry<ModelClass, FieldType, RepositoryType> implements VortexCrudCollectionFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    private final Map<Class<? extends VortexCrudCollectionFactory>, VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> factories = new HashMap<>();

    public CollectionFactoryRegistry(
                                     VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry,
                                     ReflectionService<FieldType> reflectionService,
                                     VortexCrudDataStoreUtilStrategy dataStoreUtil,
                                     ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy
    ) {
        factories.put(
                ListCollectionFactory.class,
                new ListCollectionFactory<>(
                        dialogFactoryRegistry,
                        reflectionService,
                        dataStoreUtil,
                        manyToManyPersistenceStrategy
                )
        );
    }

    public Map<Class<? extends VortexCrudCollectionFactory>, VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> key, VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> factory) {
        factories.put(key, factory);
    }
}

