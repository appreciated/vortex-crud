package com.github.appreciated.vortex_crud.core.entity.data_store;

import java.util.List;

/**
 * Interface for basic data store operations.
 *
 * @param <FieldType> The type used to identify fields in the data store
 * @param <ModelClass> The type of the model
 */
public interface VortexCrudDataStore<FieldType, ModelClass> {

    /**
     * Inserts a record into the data store.
     *
     * @param entity The entity to insert
     * @return The ID of the inserted record
     */
    Object insertRecord(ModelClass entity);

    /**
     * Gets records from the data store with pagination.
     *
     * @param offset The offset for pagination
     * @param limit  The limit for pagination
     * @return A list of records
     */
    List<ModelClass> getRecordsFromTable(int offset, int limit);

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
     * @param entity The entity with updated values
     */
    void updateRecordById(ModelClass entity);

    /**
     * Deletes a record by ID.
     *
     * @param id The ID of the record to delete
     */
    void deleteRecordById(Object id);

    /**
     * Counts the number of records in the data store.
     *
     * @return The number of records
     */
    int count();

    Class<ModelClass> getModelClass();

    void updateRecord(ModelClass entity);

    void deleteRecord(ModelClass entity);

    ModelClass newInstance();
}
