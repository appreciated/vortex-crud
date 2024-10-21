package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection;


import com.github.appreciated.turbo_crud.config.model.FormElement;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public interface TurboCrudCollectionFactory {
    Component createCollection(String id, Route route, FormElement factoryConfig, TurboCrudRouteFactoryRegistry routeFactory, FormCreator formCreator);
}
