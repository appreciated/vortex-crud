package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Interface representing a many-to-many relationship between entities.
 *
 * @param <ModelClass> The type used to identify data stores
 * @param <FieldType>  The type used to identify fields in the data store
 */
public interface ManyToMany<ModelClass, FieldType, RepositoryKey> {

    /**
     * Gets the reference field for the many-to-many relationship.
     *
     * @param collectionConfiguration The collection configuration
     * @return The reference field ID
     */
    FieldType getReferenceField(CollectionConfiguration<ModelClass, FieldType, RepositoryKey> collectionConfiguration);

    /**
     * Gets the associative data store for the many-to-many relationship.
     *
     * @return The associative data store ID
     */
    ModelClass getAssociativeDataStoreKey();

    /**
     * Gets the associative target ID field for the many-to-many relationship.
     *
     * @return The associative target ID field
     */
    FieldType getAssociativeTargetIdField();

    /**
     * Gets the associative source ID field for the many-to-many relationship.
     *
     * @return The associative source ID field
     */
    FieldType getAssociativeSourceIdField();

    RepositoryKey getDatastore();
}
