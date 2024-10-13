package com.github.appreciated.turbo_crud.ui.factories.collection;

import com.github.appreciated.turbo_crud.config.model.FormField;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudCollectionFactoryRegistry {

   TurboCrudCollectionFactory getFactory(FormField type);
   void addFactory(String key, TurboCrudCollectionFactory factory);

}