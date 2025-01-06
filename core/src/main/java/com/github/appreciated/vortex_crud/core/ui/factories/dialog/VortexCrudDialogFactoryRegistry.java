package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> {

    VortexCrudDialogFactory<DataStoreId, FieldId> getFactory(Class<?> type);

    void addFactory(Class<?> key, VortexCrudDialogFactory<DataStoreId, FieldId> factory);

}