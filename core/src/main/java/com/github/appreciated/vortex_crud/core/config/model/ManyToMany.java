package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Interface representing a many-to-many relationship between entities.
 *
 * @param <DataStoreId> The type used to identify data stores
 * @param <FieldId>     The type used to identify fields in the data store
 */
public interface ManyToMany<DataStoreId, FieldId, KeyType> {

    /**
     * Gets the reference field for the many-to-many relationship.
     *
     * @param collectionConfiguration The collection configuration
     * @return The reference field ID
     */
    FieldId getReferenceField(CollectionConfiguration<DataStoreId, FieldId, KeyType> collectionConfiguration);

    /**
     * Gets the associative data store for the many-to-many relationship.
     *
     * @return The associative data store ID
     */
    DataStoreId getAssociativeDataStoreKey();

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

    KeyType getModelClass();
}
