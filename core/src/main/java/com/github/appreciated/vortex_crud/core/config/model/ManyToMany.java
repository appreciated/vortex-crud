package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;

import java.util.List;

/**
 * Interface representing a many-to-many relationship between entities.
 *
 * @param <DataStoreId> The type used to identify data stores
 * @param <FieldId> The type used to identify fields in the data store
 */
public interface ManyToMany<DataStoreId, FieldId> {

    /**
     * Gets data from the many-to-many relationship.
     *
     * @param dataStoreFactoryRegistry The registry for data store factories
     * @param foreignKeyValue The foreign key value to filter by
     * @param dataStore The data store to retrieve records from
     * @param collectionConfiguration The collection configuration
     * @param modelClass The class of the model to retrieve
     * @return A list of entities matching the criteria
     */
    <T> List<T> getData(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, 
            String foreignKeyValue, 
            VortexCrudDataStore<FieldId> dataStore, 
            CollectionConfiguration<DataStoreId, FieldId> collectionConfiguration,
            Class<T> modelClass
    );

    /**
     * Gets the reference field for the many-to-many relationship.
     *
     * @param collectionConfiguration The collection configuration
     * @return The reference field ID
     */
    FieldId getReferenceField(CollectionConfiguration<DataStoreId, FieldId> collectionConfiguration);

    /**
     * Gets the associative data store for the many-to-many relationship.
     *
     * @return The associative data store ID
     */
    DataStoreId getAssociativeDataStore();

    /**
     * Gets the associative target ID field for the many-to-many relationship.
     *
     * @return The associative target ID field
     */
    FieldId getAssociativeTargetIdField();

    /**
     * Gets the associative source ID field for the many-to-many relationship.
     *
     * @return The associative source ID field
     */
    FieldId getAssociativeSourceIdField();
}
