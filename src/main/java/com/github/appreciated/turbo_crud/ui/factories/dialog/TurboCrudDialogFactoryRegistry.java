package com.github.appreciated.turbo_crud.ui.factories.dialog;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudDialogFactoryRegistry {

    TurboCrudDialogFactory getFactory(Class<? extends TurboCrudDialogFactory> type);

    void addFactory(Class<? extends TurboCrudDialogFactory> key, TurboCrudDialogFactory factory);

}