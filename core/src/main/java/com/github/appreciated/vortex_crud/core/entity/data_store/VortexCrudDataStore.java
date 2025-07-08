package com.github.appreciated.vortex_crud.core.entity.data_store;

import java.util.List;

/**
 * Interface for data store operations.
 * Uses reflection to work with model classes directly instead of GenericEntity.
 *
 * @param <FieldId> The type used to identify fields in the data store
 */
public interface VortexCrudDataStore<FieldId> {

    /**
     * Inserts a record into the data store.
     *
     * @param entity The entity to insert
     * @param modelClass The class of the model to insert
     * @return The ID of the inserted record
     */
    <T> Object insertRecord(T entity, Class<T> modelClass);

    /**
     * Gets records from the data store with pagination.
     *
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param modelClass The class of the model to retrieve
     * @return A list of records
     */
    <T> List<T> getRecordsFromTable(int offset, int limit, Class<T> modelClass);

    /**
     * Gets records from the data store where a column equals a value, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param modelClass The class of the model to retrieve
     * @return A list of records matching the criteria
     */
    <T> List<T> getRecordsFromTableWhereColumnEquals(FieldId filterField, Object filterValue, int offset, int limit, Class<T> modelClass);

    /**
     * Gets records from the data store where a column is in a list of values, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The list of values to filter by
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param modelClass The class of the model to retrieve
     * @return A list of records matching the criteria
     */
    <T> List<T> getRecordsFromTableWhereColumnIn(FieldId filterField, List<String> filterValue, int offset, int limit, Class<T> modelClass);

    /**
     * Gets records from the data store where a column is like a value, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param modelClass The class of the model to retrieve
     * @return A list of records matching the criteria
     */
    <T> List<T> getRecordsFromTableWhereColumnLike(FieldId filterField, Object filterValue, int offset, int limit, Class<T> modelClass);

    /**
     * Gets a record by ID.
     *
     * @param id The ID of the record to retrieve
     * @param modelClass The class of the model to retrieve
     * @return The record with the specified ID
     */
    <T> T getRecordById(Object id, Class<T> modelClass);

    /**
     * Updates a record by ID.
     *
     * @param id The ID of the record to update
     * @param entity The entity with updated values
     * @param modelClass The class of the model to update
     */
    <T> void updateRecordById(Object id, T entity, Class<T> modelClass);

    /**
     * Deletes a record by ID.
     *
     * @param id The ID of the record to delete
     */
    void deleteRecordById(Object id);

    /**
     * Deletes all records from the data store.
     */
    void deleteAllRecords();

    /**
     * Counts the number of records in the data store.
     *
     * @return The number of records
     */
    int count();

    /**
     * Counts the number of records in the data store where a column is like a value.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @return The number of records matching the criteria
     */
    int countWhereColumnLike(FieldId filterField, String filterValue);

    /**
     * Gets a field by name.
     *
     * @param foreignKeyField The name of the field
     * @return The field
     */
    java.lang.reflect.Field getField(String foreignKeyField);

    /**
     * For backward compatibility with existing code that uses GenericEntity.
     * This method should be removed once all code is migrated to use model classes directly.
     *
     * @param id The ID of the record to retrieve
     * @return The record with the specified ID as an Object
     */
    Object getRecordById(Object id);
}
