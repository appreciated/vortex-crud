package com.github.appreciated.vortex_crud.core.entity.data_store;

import java.util.List;

/**
 * Interface for basic data store operations.
 * Extends VortexCrudBaseDataStore.
 * Contains basic CRUD methods plus simple filtering capabilities (Equals, In, Like).
 *
 * @param <FieldType>  The type used to identify fields in the data store (e.g., TableField, String)
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudDataStore<FieldType, ModelClass> extends VortexCrudBaseDataStore<ModelClass> {

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

}
