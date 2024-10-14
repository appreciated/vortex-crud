package com.github.appreciated.turbo_crud.ui.factories.elements.fields;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudFieldFactoryRegistry {

    TurboCrudFieldFactory getFactory(String type);

    void addFactory(String key, TurboCrudFieldFactory factory);
}