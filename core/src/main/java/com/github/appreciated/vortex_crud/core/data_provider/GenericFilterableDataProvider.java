package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.stream.Stream;

/**
 * A generic data provider that bridges Vaadin's {@link CallbackDataProvider} with
 * {@link VortexCrudDataStore}. It supports filtering by a specific field.
 *
 * @param <FieldType> The type of the field identifier (e.g., String for JPA, TableField for jOOQ).
 */
public class GenericFilterableDataProvider<FieldType> extends CallbackDataProvider<Object, String> {

    /**
     * Creates a new instance of {@code GenericFilterableDataProvider}.
     *
     * @param dataStore   The data store to fetch data from.
     * @param filterField The field to apply filtering on. If null, no filtering is applied.
     */
    public GenericFilterableDataProvider(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField) {
        super(
                query -> fetchFromDataStore(dataStore, filterField, query),
                query -> countFromDataStore(dataStore, filterField, query)
        );
    }

    @SuppressWarnings("unchecked")
    private static <FieldType> Stream<Object> fetchFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
        if (filterText.isEmpty() || filterField == null) {
            return (Stream<Object>) dataStore.getRecordsFromTable(query.getOffset(), query.getLimit()).stream();
        } else {
            return (Stream<Object>) dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream();
        }
    }

    private static <FieldType> int countFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
        if (filterText.isEmpty() || filterField == null) {
            return dataStore.count();
        } else {
            return dataStore.countWhereColumnLike(filterField, filterText);
        }
    }
}
