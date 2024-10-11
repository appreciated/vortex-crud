package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;

public interface FlowCmsCollectionFactory {
    Component createCollection(String table, String field, FieldConfig fieldConfig);
}
