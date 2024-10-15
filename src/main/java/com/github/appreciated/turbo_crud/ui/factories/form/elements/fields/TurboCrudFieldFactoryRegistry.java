package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudFieldFactoryRegistry {

    TurboCrudFieldFactory getFactory(String type);

    void addFactory(String key, TurboCrudFieldFactory factory);
}