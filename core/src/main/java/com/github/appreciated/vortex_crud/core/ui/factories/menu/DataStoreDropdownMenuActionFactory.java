package com.github.appreciated.vortex_crud.core.ui.factories.menu;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.components.DataStoreDropdown;
import com.github.appreciated.vortex_crud.core.ui.components.DataStoreDropdownConfig;
import com.vaadin.flow.component.Component;

import java.util.Map;

/**
 * Factory for creating a DataStoreDropdown menu action component.
 * This factory creates a dropdown that fetches its data from a VortexCrudDataStore
 * using the configured filter parameters.
 *
 * @param <ModelClass> The type of entity
 * @param <FieldType> The type used to identify fields
 * @param <RepositoryType> The type of repository key
 */
public class DataStoreDropdownMenuActionFactory<ModelClass, FieldType, RepositoryType>
        implements MenuActionComponentFactory<ModelClass, FieldType, RepositoryType> {

    private final DataStoreDropdownConfig<ModelClass, FieldType, RepositoryType> config;
    private final Map<RepositoryType, VortexCrudDataStore<FieldType, ?>> dataStores;

    /**
     * Creates a new DataStoreDropdownMenuActionFactory.
     *
     * @param config The configuration for the dropdown
     * @param dataStores Map of available data stores
     */
    public DataStoreDropdownMenuActionFactory(
            DataStoreDropdownConfig<ModelClass, FieldType, RepositoryType> config,
            Map<RepositoryType, VortexCrudDataStore<FieldType, ?>> dataStores) {
        this.config = config;
        this.dataStores = dataStores;
    }

    @Override
    public Component get() {
        VortexCrudDataStore<FieldType, ModelClass> typedDataStore;

        if (config.dataStore() != null) {
            typedDataStore = config.dataStore();
        } else {
            VortexCrudDataStore<FieldType, ?> dataStore = dataStores.get(config.dataStoreKey());

            if (dataStore == null) {
                throw new IllegalArgumentException(
                        "No data store found for key: " + config.dataStoreKey()
                );
            }

            // Cast is safe because we're using the same data store that was registered
            @SuppressWarnings("unchecked")
            VortexCrudDataStore<FieldType, ModelClass> castedDataStore =
                    (VortexCrudDataStore<FieldType, ModelClass>) dataStore;
            typedDataStore = castedDataStore;
        }

        return new DataStoreDropdown<>(config, typedDataStore);
    }
}
