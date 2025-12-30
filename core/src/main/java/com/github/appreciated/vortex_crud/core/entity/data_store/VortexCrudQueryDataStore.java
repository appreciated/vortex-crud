package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;

import java.util.List;

/**
 * Interface for data store operations with advanced query capabilities.
 * Extends VortexCrudDataStore with filtering, searching, and ordering features.
 * Use this for database-backed implementations (JPA, jOOQ) that support complex queries.
 *
 * @param <FieldType>  The type used to identify fields in the data store (e.g., TableField, String)
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudQueryDataStore<FieldType, ModelClass> extends VortexCrudDataStore<FieldType, ModelClass> {

    /**
     * Gets records from the data store where a column equals a value, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnEquals(FieldType filterField, Object filterValue, int offset, int limit);

    /**
     * Gets records from the data store where a column equals a value and orders the result by another column.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param orderField  The field to order the results by (ascending)
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of ordered records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(FieldType filterField, Object filterValue, FieldType orderField, int offset, int limit);

    /**
     * Gets records from the data store where a column is in a list of values, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The list of values to filter by
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnIn(FieldType filterField, List<String> filterValue, int offset, int limit);

    /**
     * Gets records from the data store where a column is like a value, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnLike(FieldType filterField, Object filterValue, int offset, int limit);


    /**
     * Counts the number of records in the data store where a column is like a value.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @return The number of records matching the criteria
     */
    int countWhereColumnLike(FieldType filterField, String filterValue);

    /**
     * Counts the number of records in the data store where filters match.
     *
     * @param filters The filters to apply (AND logic)
     * @return The number of records matching the criteria
     */
    int countWhereFiltersEqual(java.util.List<RouteFilter<FieldType>> filters);

    /**
     * Gets records from the data store where filters match, with pagination.
     *
     * @param filters The filters to apply (AND logic)
     * @param offset  The offset for pagination
     * @param limit   The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereFiltersEqual(java.util.List<RouteFilter<FieldType>> filters, int offset, int limit);

    /**
     * Gets records from the data store where a column is like a value AND filters match, with pagination.
     *
     * @param searchField The field to search (like)
     * @param searchValue The value to search for
     * @param filters     The filters to apply (AND logic)
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnLikeAndFiltersEqual(FieldType searchField, Object searchValue, java.util.List<RouteFilter<FieldType>> filters, int offset, int limit);

    /**
     * Counts the number of records in the data store where a column is like a value AND filters match.
     *
     * @param searchField The field to search (like)
     * @param searchValue The value to search for
     * @param filters     The filters to apply (AND logic)
     * @return The number of records matching the criteria
     */
    int countWhereColumnLikeAndFiltersEqual(FieldType searchField, String searchValue, java.util.List<RouteFilter<FieldType>> filters);
}
