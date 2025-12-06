package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.List;

/**
 * A dropdown component that fetches its data from a VortexCrudDataStore.
 * This component uses the {@link VortexCrudDataStore#getRecordsFromTableWhereColumnEquals} method
 * to filter and fetch data based on the provided configuration.
 *
 * @param <ModelClass> The type of entity in the data store
 * @param <FieldType> The type used to identify fields in the data store
 * @param <RepositoryType> The type of repository key
 */
public class DataStoreDropdown<ModelClass, FieldType, RepositoryType> extends ComboBox<ModelClass> {

    private final DataStoreDropdownConfig<ModelClass, FieldType, RepositoryType> config;
    private final VortexCrudDataStore<FieldType, ModelClass> dataStore;

    /**
     * Creates a new DataStoreDropdown with the given configuration and data store.
     *
     * @param config The configuration for this dropdown
     * @param dataStore The data store to fetch data from
     */
    public DataStoreDropdown(
            DataStoreDropdownConfig<ModelClass, FieldType, RepositoryType> config,
            VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        this.config = config;
        this.dataStore = dataStore;

        configure();
        loadData();
    }

    /**
     * Configures the dropdown based on the provided configuration.
     */
    private void configure() {
        if (config.label() != null) {
            setLabel(config.label());
        }

        if (config.placeholder() != null) {
            setPlaceholder(config.placeholder());
        }

        setReadOnly(config.readOnly());
        setRequired(config.required());

        // Set up the item label generator
        if (config.labelProvider() != null) {
            setItemLabelGenerator(config.labelProvider()::apply);
        } else {
            setItemLabelGenerator(item -> item != null ? item.toString() : "");
        }
    }

    /**
     * Loads data from the data store using the configured filter.
     */
    private void loadData() {
        List<ModelClass> items;

        if (config.filterField() != null && config.filterValue() != null) {
            // Use filtered query
            items = dataStore.getRecordsFromTableWhereColumnEquals(
                    config.filterField(),
                    config.filterValue(),
                    0,
                    config.limit()
            );
        } else {
            // Use unfiltered query
            items = dataStore.getRecordsFromTable(0, config.limit());
        }

        setItems(items);
    }

    /**
     * Reloads the data from the data store.
     * This can be called when the underlying data has changed.
     */
    public void refresh() {
        loadData();
    }

    /**
     * Gets the configuration used by this dropdown.
     *
     * @return The dropdown configuration
     */
    public DataStoreDropdownConfig<ModelClass, FieldType, RepositoryType> getConfig() {
        return config;
    }

    /**
     * Gets the data store used by this dropdown.
     *
     * @return The data store
     */
    public VortexCrudDataStore<FieldType, ModelClass> getDataStore() {
        return dataStore;
    }
}
