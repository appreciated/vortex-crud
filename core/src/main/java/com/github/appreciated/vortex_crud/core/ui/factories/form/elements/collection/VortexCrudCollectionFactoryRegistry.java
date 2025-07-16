package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, KeyType> {

    VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType> getFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> factory);

    void addFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType>> key, VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType> factory);

}