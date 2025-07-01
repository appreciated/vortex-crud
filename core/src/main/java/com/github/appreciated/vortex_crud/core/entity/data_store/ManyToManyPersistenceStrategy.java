package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;

import java.util.List;

/**
 * Strategy interface for retrieving records from a data store.
 * Different implementations can be provided for different data store types (e.g., JPA, jOOQ).
 *
 * @param <FieldId> The type used to identify fields in the data store
 */
public interface ManyToManyPersistenceStrategy<DataStoreId, FieldId> {
    
    /**
     * Retrieves records from a data store where a specific column equals a given value.
     *
     * @param dataStore The data store to retrieve records from
     * @return A list of records matching the criteria
     */
    List<GenericEntity> getManyToMany(
            VortexCrudDataStore<FieldId> dataStore,
            ManyToMany<DataStoreId, FieldId> manyToMany
    );

    void insert(List<GenericEntity> genericEntities);

    void deleteAll(List<GenericEntity> list);
}