package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> {

    VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass> getFactory(Class<?> type);

    void addFactory(Class<?> key, VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass> factory);

}