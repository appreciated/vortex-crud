package com.github.appreciated.flow_cms.ui.fields;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface FlowCmsFieldFactory {

    <Comp extends Component & HasValue> Comp createComponent(String table, String field, FieldConfig type);

}