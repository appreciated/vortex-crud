package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.core.config.model.InternalFormElement;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public interface TurboCrudCollectionFactory<DataStoreId, FieldId> {
    Component createCollection(String foreignKey,
                               Route<DataStoreId, FieldId> route,
                               InternalFormElement<DataStoreId, FieldId> factoryConfig,
                               TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                               FormCreator formCreator);
}
