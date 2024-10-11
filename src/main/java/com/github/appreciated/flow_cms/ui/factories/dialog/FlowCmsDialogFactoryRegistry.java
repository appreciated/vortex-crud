package com.github.appreciated.flow_cms.ui.factories.dialog;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface FlowCmsDialogFactoryRegistry {

   FlowCmsDialogFactory getFactory(String type);
   void addFactory(String key, FlowCmsDialogFactory factory);

}