package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * A generic data provider that bridges Vaadin's {@link CallbackDataProvider} with
 * {@link VortexCrudDataStore}. It supports filtering by a specific field.
 *
 * @param <FieldType> The type of the field identifier (e.g., String for JPA, TableField for jOOQ).
 */
@Slf4j
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
        // Check for route filters (advanced query capability)
        if (routeFilters != null && !routeFilters.isEmpty()) {
            if (dataStore instanceof VortexCrudQueryDataStore) {
                VortexCrudQueryDataStore<FieldType, ?> queryDataStore = (VortexCrudQueryDataStore<FieldType, ?>) dataStore;
                if ((filterText.isEmpty() || filterField == null)) {
                    return queryDataStore.getRecordsFromTableWhereFiltersEqual(routeFilters, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
                } else {
                    return queryDataStore.getRecordsFromTableWhereColumnLikeAndFiltersEqual(filterField, filterText, routeFilters, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
                }
            } else {
                log.warn("Advanced filtering (RouteFilter) is not supported by this DataStore implementation: {}", dataStore.getClass().getName());
                // Fallback: fetch all and filter in memory? Or just ignore filter?
                // Ignoring filters for safety in fallback, or we could rely on base filtering if applicable.
                // But base filtering only supports Like on one column.
                // If filterText is present, at least use that.
                if ((!filterText.isEmpty() && filterField != null)) {
                    return dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
                } else {
                    return dataStore.getRecordsFromTable(query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
                }
            }
        } else {
            // Simple filtering or no filtering
            if ((!filterText.isEmpty() && filterField != null)) {
                return dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
            } else {
                return dataStore.getRecordsFromTable(query.getOffset(), query.getLimit()).stream().map(obj -> (Object) obj);
            }
        }
    }

    private static <FieldType> int countFromDataStore(VortexCrudDataStore<FieldType, ?> dataStore, FieldType filterField, java.util.List<RouteFilter<FieldType>> routeFilters, Query<Object, String> query) {
        String filterText = query.getFilter().orElse("");
         // Check for route filters (advanced query capability)
        if (routeFilters != null && !routeFilters.isEmpty()) {
            if (dataStore instanceof VortexCrudQueryDataStore) {
                VortexCrudQueryDataStore<FieldType, ?> queryDataStore = (VortexCrudQueryDataStore<FieldType, ?>) dataStore;
                if ((filterText.isEmpty() || filterField == null)) {
                    return queryDataStore.countWhereFiltersEqual(routeFilters);
                } else {
                    return queryDataStore.countWhereColumnLikeAndFiltersEqual(filterField, filterText, routeFilters);
                }
            } else {
                // Fallback
                if ((!filterText.isEmpty() && filterField != null)) {
                    return dataStore.countWhereColumnLike(filterField, filterText);
                } else {
                    return dataStore.count();
                }
            }
        } else {
            // Simple filtering or no filtering
            if ((!filterText.isEmpty() && filterField != null)) {
                return dataStore.countWhereColumnLike(filterField, filterText);
            } else {
                return dataStore.count();
            }
        }
    }
}
