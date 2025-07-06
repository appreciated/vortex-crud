package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, ModelClass> {

    VortexCrudCollectionFactory<DataStoreId, FieldId, ModelClass> getFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, ModelClass>> factory);

    void addFactory(Class<? extends VortexCrudCollectionFactory<DataStoreId, FieldId, ModelClass>> key, VortexCrudCollectionFactory<DataStoreId, FieldId, ModelClass> factory);

}