package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> {

    TurboCrudDialogFactory<DataStoreId, FieldId> getFactory(Class<?> type);

    void addFactory(Class<?> key, TurboCrudDialogFactory<DataStoreId, FieldId> factory);

}