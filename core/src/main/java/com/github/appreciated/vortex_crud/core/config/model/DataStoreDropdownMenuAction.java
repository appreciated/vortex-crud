package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Configuration model for a data store dropdown menu action.
 * This allows adding dropdown components to route menus that fetch data from a VortexCrudDataStore.
 *
 * @param <FieldType> The type used to identify fields
 * @param <RepositoryType> The type of repository key
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType> implements I18nKeyProvider {

    /**
     * The data store config to use for fetching data
     */
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

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
     * The field to use as the display label in the dropdown.
     * If not provided, toString() will be used on the entity.
     */
    private FieldType labelField;

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

    @Override
    public Collection<String> getI18nKeys() {
        List<String> keys = new ArrayList<>();
        if (label != null) keys.add(label);
        if (placeholder != null) keys.add(placeholder);
        return keys;
    }
}