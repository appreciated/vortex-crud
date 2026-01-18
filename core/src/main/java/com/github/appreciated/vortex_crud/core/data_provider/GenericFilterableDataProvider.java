package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
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
     * @param dataStore      The data store to fetch data from.
     * @param filterField    The field to apply filtering on. If null, no filtering is applied.
     * @param filters The default filters to apply.
     */
    public GenericFilterableDataProvider(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> filters) {
        super(
                query -> fetchFromDataStore(dataStore, filterField, filters, query),
                query -> countFromDataStore(dataStore, filterField, filters, query)
        );
    }

    private static <FieldType> Stream<Object> fetchFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> filters, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
        if ((filterText.isEmpty() || filterField == null) && (filters == null || filters.isEmpty())) {
            return dataStore.getRecordsFromTable(query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
        } else if (filterText.isEmpty() || filterField == null) {
            return dataStore.getRecordsFromTableWhereFiltersEqual(filters, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
        } else if (filters != null && !filters.isEmpty()) {
            return dataStore.getRecordsFromTableWhereColumnLikeAndFiltersEqual(filterField, filterText, filters, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
        } else {
            return dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
        }
    }

    private static <FieldType> int countFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> filters, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
        if ((filterText.isEmpty() || filterField == null) && (filters == null || filters.isEmpty())) {
            return dataStore.count();
        } else if (filterText.isEmpty() || filterField == null) {
            return dataStore.countWhereFiltersEqual(filters);
        } else if (filters != null && !filters.isEmpty()) {
            return dataStore.countWhereColumnLikeAndFiltersEqual(filterField, filterText, filters);
        } else {
            return dataStore.countWhereColumnLike(filterField, filterText);
        }
    }
}
