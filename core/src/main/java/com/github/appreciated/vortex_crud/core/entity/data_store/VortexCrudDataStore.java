package com.github.appreciated.vortex_crud.core.entity.data_store;

import java.util.List;

/**
 * Interface for data store operations.
 * Uses reflection to work with model classes directly instead of GenericEntity.
 *
 * @param <FieldId> The type used to identify fields in the data store
 */
public interface VortexCrudDataStore<FieldId, ModelClass> {

    Class<ModelClass> getModelClass();

    /**
     * Inserts a record into the data store.
     *
     * @param entity The entity to insert
     * @return The ID of the inserted record
     */
    ModelClass insertRecord(ModelClass entity);

    /**
     * Gets records from the data store with pagination.
     *
     * @param offset The offset for pagination
     * @param limit  The limit for pagination
     * @return A list of records
     */
    List<ModelClass> getRecordsFromTable(int offset, int limit);

    /**
     * Gets records from the data store where a column equals a value, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnEquals(FieldId filterField, Object filterValue, int offset, int limit);

    /**
     * Gets records from the data store where a column is in a list of values, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The list of values to filter by
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnIn(FieldId filterField, List<String> filterValue, int offset, int limit);

    /**
     * Gets records from the data store where a column is like a value, with pagination.
     *
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset      The offset for pagination
     * @param limit       The limit for pagination
     * @return A list of records matching the criteria
     */
    List<ModelClass> getRecordsFromTableWhereColumnLike(FieldId filterField, Object filterValue, int offset, int limit);

    /**
     * Gets a record by ID.
     *
     * @param id The ID of the record to retrieve
     * @return The record with the specified ID
     */
    ModelClass getRecordById(Object id);

    /**
     * Updates a record by ID.
     *
     * @param id     The ID of the record to update
     * @param entity The entity with updated values
     */
    void updateRecordById(Object id, ModelClass entity);

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

    default ModelClass createModelInstance() {
        try {
            return getModelClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
