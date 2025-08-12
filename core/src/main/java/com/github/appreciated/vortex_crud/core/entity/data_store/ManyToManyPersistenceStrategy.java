package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;

import java.util.List;

/**
 * Strategy interface for retrieving records from a data store.
 * Different implementations can be provided for different data store types (e.g., JPA, jOOQ).
 *
 * @param <DataStoreId> The type used to identify data stores
 * @param <FieldId>     The type used to identify fields in the data store
 */
public interface ManyToManyPersistenceStrategy<DataStoreId, FieldId, KeyType> {

    /**
     * Retrieves records from a data store based on many-to-many relationship.
     *
     * @param targetDataStore The data store for which n:n records need to be retrieved. This can be handy f.e. JPA.
     * @param manyToMany      The many-to-many relationship configuration containing implementation-specific
     *                        configuration to retrieve the necessary data
     * @return A list of records matching the criteria
     */
    java.util.Collection<DataStoreId> resolveManyToMany(
            VortexCrudDataStore<FieldId, ?> targetDataStore,
            ManyToMany<DataStoreId, FieldId, KeyType> manyToMany,
            Object sourceId
    );

    /**
     * Connects a list of new relations for a datapoint.
     *
     * @param sourceId The ID of the source entity
     * @param targetObjects The target objects to connect to the source entity
     * @param manyToMany The many-to-many relationship configuration
     */
    void insert(Object sourceId, List<Object> targetObjects, ManyToMany<DataStoreId, FieldId, KeyType> manyToMany);

    /**
     * Removes a list of entities from a relation for a datapoint.
     *
     * @param sourceId The ID of the source entity
     * @param targetObjects The target objects to disconnect from the source entity
     * @param manyToMany The many-to-many relationship configuration
     */
    void deleteAll(Object sourceId, List<Object> targetObjects, ManyToMany<DataStoreId, FieldId, KeyType> manyToMany);
    
    /**
     * Retrieves the ID of an object.
     *
     * @param object The object to get the ID from
     * @return The ID of the object
     */
    String getObjectId(Object object);
}
