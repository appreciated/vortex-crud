package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.core.config.model.InternalFormElement;
import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public interface TurboCrudCollectionFactory<DataStoreId, FieldId> {
    Component createCollection(String foreignKey,
                               RouteRenderer<DataStoreId, FieldId> routeRenderer,
                               InternalFormElement<DataStoreId, FieldId> factoryConfig,
                               TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                               FormCreator<DataStoreId, FieldId> formCreator);
}
