package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.FieldConfig;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface FlowCmsCollectionFactoryRegistry {

   FlowCmsCollectionFactory getFactory(FieldConfig type);
   void addFactory(String key, FlowCmsCollectionFactory factory);

}