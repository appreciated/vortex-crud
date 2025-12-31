package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;

import java.util.List;

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
    FieldType referenceField(CollectionConfiguration<ModelClass, FieldType, RepositoryKey> collectionConfiguration);

    /**
     * Gets the associative data store for the many-to-many relationship.
     *
     * @return The associative data store ID
     */
    ModelClass associativeDataStoreKey();

    /**
     * Gets the associative target ID field for the many-to-many relationship.
     *
     * @return The associative target ID field
     */
    FieldType associativeTargetIdField();

    /**
     * Gets the associative source ID field for the many-to-many relationship.
     *
     * @return The associative source ID field
     */
    FieldType associativeSourceIdField();

    RepositoryKey datastore();


    public VortexCrudQueryDataStore<FieldType, ModelClass> dataStoreInstance();

    OneToMany<ModelClass, FieldType, RepositoryKey> oneToMany();

    ManyToMany<ModelClass, FieldType, RepositoryKey> manyToMany();

    List<FieldType> children();
}
