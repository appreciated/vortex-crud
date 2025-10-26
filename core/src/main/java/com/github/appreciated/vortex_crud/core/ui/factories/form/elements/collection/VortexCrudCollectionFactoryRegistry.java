package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudCollectionFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> factory);

    void addFactory(Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> key, VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> factory);

}