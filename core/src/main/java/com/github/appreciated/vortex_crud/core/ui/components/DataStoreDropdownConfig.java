package com.github.appreciated.vortex_crud.core.ui.components;

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

    public String label() {
        return label;
    }

    public RepositoryType dataStoreKey() {
        return dataStoreKey;
    }

    public FieldType filterField() {
        return filterField;
    }

    public Object filterValue() {
        return filterValue;
    }

    public int limit() {
        return limit;
    }

    public SerializableFunction<ModelClass, String> labelProvider() {
        return labelProvider;
    }

    public String placeholder() {
        return placeholder;
    }

    public boolean readOnly() {
        return readOnly;
    }

    public boolean required() {
        return required;
    }
}
