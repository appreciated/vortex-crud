package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public interface VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType> {
    Component createCollection(String foreignKey,
                               RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                               InternalFormElement<DataStoreId, FieldId, KeyType> factoryConfig,
                               VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                               FormCreator<DataStoreId, FieldId, KeyType> formCreator);
}
