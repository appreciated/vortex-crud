package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.FormField;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudCollectionFactoryRegistry {

   TurboCrudCollectionFactory getFactory(FormField type);
   void addFactory(String key, TurboCrudCollectionFactory factory);

}