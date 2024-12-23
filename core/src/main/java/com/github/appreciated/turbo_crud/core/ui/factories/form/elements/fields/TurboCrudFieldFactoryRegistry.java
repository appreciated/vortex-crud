package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudFieldFactoryRegistry {

    TurboCrudFieldFactory getFactory(Class<? extends TurboCrudFieldFactory> type);

    void addFactory(Class<? extends TurboCrudFieldFactory> key, TurboCrudFieldFactory factory);
}