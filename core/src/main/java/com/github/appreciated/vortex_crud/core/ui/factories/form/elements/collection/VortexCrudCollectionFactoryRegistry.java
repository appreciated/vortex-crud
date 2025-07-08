package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId> {

    VortexCrudCollectionFactory<DataStoreId, FieldId> getFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId>> factory);

    void addFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId>> key, VortexCrudCollectionFactory<DataStoreId, FieldId> factory);

}