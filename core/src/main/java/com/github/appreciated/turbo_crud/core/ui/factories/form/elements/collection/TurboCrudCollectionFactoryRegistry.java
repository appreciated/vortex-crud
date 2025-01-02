package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudCollectionFactoryRegistry<DataStoreId, FieldId> {

    TurboCrudCollectionFactory<DataStoreId, FieldId> getFactory(Class<? extends TurboCrudCollectionFactory<DataStoreId, FieldId>> factory);

    void addFactory(Class<? extends TurboCrudCollectionFactory<DataStoreId, FieldId>> key, TurboCrudCollectionFactory<DataStoreId, FieldId> factory);

}