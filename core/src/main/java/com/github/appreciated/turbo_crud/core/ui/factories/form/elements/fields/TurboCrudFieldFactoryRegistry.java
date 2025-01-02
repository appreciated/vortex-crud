package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudFieldFactoryRegistry<DataStoreId, FieldId> {

    TurboCrudFieldFactory<DataStoreId, FieldId> getFactory(Class<? extends TurboCrudFieldFactory> type);

    void addFactory(Class<? extends TurboCrudFieldFactory> key, TurboCrudFieldFactory<DataStoreId, FieldId> factory);
}