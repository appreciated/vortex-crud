package com.github.appreciated.vortex_crud.core.entity.data_store;

import java.util.List;

/**
 * Base interface for simple data store operations.
 * Provides only essential CRUD operations without complex query requirements.
 * Perfect for custom implementations like file systems, in-memory stores, or external APIs.
 *
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudBaseDataStore<ModelClass> {

    /**
     * Creates a new instance of the model class.
     *
     * @return A new instance of ModelClass
     */
    ModelClass newInstance();

    /**
     * Inserts a record into the data store.
     *
     * @param entity The entity to insert
     * @return The ID of the inserted record
     */
    Object insertRecord(ModelClass entity);

    /**
     * Gets a record by its ID.
     *
     * @param id The ID of the record to retrieve
     * @return The record with the specified ID, or null if not found
     */
    ModelClass getRecordById(Object id);

    /**
     * Gets all records from the data store with pagination.
     *
     * @param offset The offset for pagination
     * @param limit  The limit for pagination
     * @return A list of records
     */
    List<ModelClass> getRecordsFromTable(int offset, int limit);

    /**
     * Updates an existing record.
     *
     * @param entity The entity with updated values
     */
    void updateRecord(ModelClass entity);

    /**
     * Updates a record by its ID.
     *
     * @param entity The entity with updated values
     */
    void updateRecordById(ModelClass entity);

    /**
     * Deletes a record.
     *
     * @param entity The entity to delete
     */
    void deleteRecord(ModelClass entity);

    /**
     * Deletes a record by its ID.
     *
     * @param id The ID of the record to delete
     */
    void deleteRecordById(Object id);

    /**
     * Counts the total number of records in the data store.
     *
     * @return The number of records
     */
    int count();

    /**
     * Gets the model class managed by this data store.
     *
     * @return The class object for ModelClass
     */
    Class<ModelClass> getModelClass();
}
