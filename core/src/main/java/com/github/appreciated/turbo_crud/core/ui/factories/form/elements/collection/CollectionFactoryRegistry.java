package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link TurboCrudCollectionFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link Field},
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class CollectionFactoryRegistry<DataStoreId, FieldId> implements TurboCrudCollectionFactoryRegistry<DataStoreId, FieldId> {

    private final Map<Class<? extends TurboCrudCollectionFactory>, TurboCrudCollectionFactory> factories = new HashMap<>();

    public CollectionFactoryRegistry(TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry, TurboCrudDataStoreFieldNameResolver<FieldId> resolver) {
        factories.put(ListCollectionFactory.class, new ListCollectionFactory<DataStoreId, FieldId>(dataStoreFactoryRegistry, dialogFactoryRegistry, resolver));
    }

    public Map<Class<? extends TurboCrudCollectionFactory>, TurboCrudCollectionFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudCollectionFactory<DataStoreId, FieldId> getFactory(Class<? extends TurboCrudCollectionFactory<DataStoreId, FieldId>> factory) {
        return Optional.ofNullable(factories.get(factory)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), factory)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudCollectionFactory<DataStoreId, FieldId>> key, TurboCrudCollectionFactory<DataStoreId, FieldId> factory) {
        factories.put(key, factory);
    }
}

