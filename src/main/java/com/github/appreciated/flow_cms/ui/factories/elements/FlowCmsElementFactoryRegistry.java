package com.github.appreciated.flow_cms.ui.factories.elements;

import com.github.appreciated.flow_cms.config.model.FieldConfig;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface FlowCmsElementFactoryRegistry {

   FlowCmsElementFactory getFactory(FieldConfig type);

}