package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;

public interface TurboCrudCollectionFactory {
    Component createCollection(String id, CollectionFactoryConfig factoryConfig, TurboCrudDetailFactoryRegistry detailFactoryRegistry, DetailFactory detailFactory, FormCreator formCreator);
}
