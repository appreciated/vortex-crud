package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;

import java.util.List;

/**
 * Strategy interface for retrieving records from a data store.
 * Different implementations can be provided for different data store types (e.g., JPA, jOOQ).
 *
 * @param <DataStoreId> The type used to identify data stores
 * @param <FieldId> The type used to identify fields in the data store
 * @param <T> The model class type
 */
public interface ManyToManyPersistenceStrategy<DataStoreId, FieldId, T> {

    /**
     * Retrieves records from a data store based on many-to-many relationship.
     *
     * @param dataStore The data store to retrieve records from
     * @param manyToMany The many-to-many relationship configuration
     * @param modelClass The class of the model to retrieve
     * @return A list of records matching the criteria
     */
    List<T> getManyToMany(
            VortexCrudDataStore<FieldId> dataStore,
            ManyToMany<DataStoreId, FieldId> manyToMany,
            Class<T> modelClass
    );

    /**
     * Inserts a list of entities into the data store.
     *
     * @param entities The entities to insert
     * @param modelClass The class of the model to insert
     */
    <E> void insert(List<E> entities, Class<E> modelClass);

    /**
     * Deletes all entities in the provided list from the data store.
     *
     * @param entities The entities to delete
     * @param modelClass The class of the model to delete
     */
    <E> void deleteAll(List<E> entities, Class<E> modelClass);
}
