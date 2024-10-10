package com.github.appreciated.flow_cms.ui.factories.fields;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.ui.factories.elements.FlowCmsElementFactory;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface FlowCmsFieldFactoryRegistry {

   FlowCmsFieldFactory getFactory(FieldConfig type);

   void addFactory(String key, FlowCmsFieldFactory factory);
}