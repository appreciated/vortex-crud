package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.vaadin.flow.component.Component;

public interface FlowCmsCollectionFactory {
    Component createCollection(String id, CollectionFactoryConfig factoryConfig, FlowCmsDetailFactoryRegistry detailFactoryRegistry);
}
