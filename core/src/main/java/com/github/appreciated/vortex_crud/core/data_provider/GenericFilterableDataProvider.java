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
     * @param routeFilters The default filters to apply.
     */
    public GenericFilterableDataProvider(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> routeFilters) {
        super(
                query -> fetchFromDataStore(dataStore, filterField, routeFilters, query),
                query -> countFromDataStore(dataStore, filterField, routeFilters, query)
        );
    }

    private static <FieldType> Stream<Object> fetchFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> routeFilters, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
        boolean hasFilterText = !filterText.isEmpty() && filterField != null;
        boolean hasRouteFilters = routeFilters != null && !routeFilters.isEmpty();
        int offset = query.getOffset();
        int limit = query.getLimit();

        if (hasFilterText && hasRouteFilters) {
            return dataStore.getRecordsFromTableWhereColumnLikeAndFiltersEqual(filterField, filterText, routeFilters, offset, limit).stream().map(obj -> (Object) obj);
        } else if (hasFilterText) {
            return dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, offset, limit).stream().map(obj -> (Object) obj);
        } else if (hasRouteFilters) {
            return dataStore.getRecordsFromTableWhereFiltersEqual(routeFilters, offset, limit).stream().map(obj -> (Object) obj);
        } else {
            return dataStore.getRecordsFromTable(offset, limit).stream().map(obj -> (Object) obj);
        }
    }

    private static <FieldType> int countFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> routeFilters, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
        boolean hasFilterText = !filterText.isEmpty() && filterField != null;
        boolean hasRouteFilters = routeFilters != null && !routeFilters.isEmpty();

        if (hasFilterText && hasRouteFilters) {
            return dataStore.countWhereColumnLikeAndFiltersEqual(filterField, filterText, routeFilters);
        } else if (hasFilterText) {
            return dataStore.countWhereColumnLike(filterField, filterText);
        } else if (hasRouteFilters) {
            return dataStore.countWhereFiltersEqual(routeFilters);
        } else {
            return dataStore.count();
        }
    }
}
