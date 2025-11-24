package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.function.SerializableFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Configuration for a data store dropdown component.
 * This configuration allows filtering data from a data store using a specific field and value.
 *
 * @param <ModelClass> The type of entity in the data store
 * @param <FieldType> The type used to identify fields in the data store
 * @param <RepositoryType> The type of repository key
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DataStoreDropdownConfig<ModelClass, FieldType, RepositoryType> {

    /**
     * The data store key to use for fetching data
     */
    private RepositoryType dataStoreKey;

    /**
     * The data store instance (optional, if dataStoreKey is not provided)
     */
    private VortexCrudDataStore<FieldType, ModelClass> dataStore;

    public static class DataStoreDropdownConfigBuilder<ModelClass, FieldType, RepositoryType> {
        private VortexCrudDataStore<FieldType, ModelClass> dataStore;

        public DataStoreDropdownConfigBuilder<ModelClass, FieldType, RepositoryType> dataStore(VortexCrudDataStore<FieldType, ModelClass> dataStore) {
            this.dataStore = dataStore;
            return this;
        }
    }

    /**
     * The field to filter on in the data store query
     */
    private FieldType filterField;

    /**
     * The value to filter by in the data store query
     */
    private Object filterValue;

    /**
     * Maximum number of items to fetch (default: 100)
     */
    @Builder.Default
    private int limit = 100;

    /**
     * Function to convert model entity to display label
     * If not provided, toString() will be used
     */
    private SerializableFunction<ModelClass, String> labelProvider;

    /**
     * Placeholder text for the dropdown when no item is selected
     */
    private String placeholder;

    /**
     * Whether the dropdown should be read-only
     */
    @Builder.Default
    private boolean readOnly = false;

    /**
     * Whether the dropdown should be required
     */
    @Builder.Default
    private boolean required = false;

    /**
     * Label for the dropdown field
     */
    private String label;
}
