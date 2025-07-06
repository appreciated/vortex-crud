package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public interface VortexCrudCollectionFactory<DataStoreId, FieldId, ModelClass> {
    Component createCollection(String foreignKey,
                               RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer,
                               InternalFormElement<DataStoreId, FieldId, ModelClass> factoryConfig,
                               VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory,
                               FormCreator<DataStoreId, FieldId, ModelClass> formCreator);
}
