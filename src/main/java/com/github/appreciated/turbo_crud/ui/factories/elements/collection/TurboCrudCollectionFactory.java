package com.github.appreciated.turbo_crud.ui.factories.elements.collection;

import com.github.appreciated.turbo_crud.config.model.DialogConfig;
import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.config.model.FormElement;
import com.github.appreciated.turbo_crud.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;

public interface TurboCrudCollectionFactory {
    Component createCollection(String id, FormElement factoryConfig, TurboCrudDetailFactoryRegistry detailFactoryRegistry, DetailFactory detailFactory, FormCreator formCreator);
}
