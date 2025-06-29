package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;

import java.util.List;

/**
 * Strategy interface for retrieving records from a data store.
 * Different implementations can be provided for different data store types (e.g., JPA, jOOQ).
 *
 * @param <FieldId> The type used to identify fields in the data store
 */
public interface RecordRetrievalStrategy<FieldId> {
    
    /**
     * Retrieves records from a data store where a specific column equals a given value.
     *
     * @param dataStore The data store to retrieve records from
     * @param filterField The field to filter on
     * @param filterValue The value to filter by
     * @param offset The offset for pagination
     * @param limit The maximum number of records to return
     * @return A list of records matching the criteria
     */
    List<GenericEntity> getRecordsWhereColumnEquals(
            VortexCrudDataStore<FieldId> dataStore,
            FieldId filterField, 
            Object filterValue, 
            int offset, 
            int limit
    );
}