package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;

public interface VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> {
    Component createCollection(Object foreignKeyValue,
                               RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                               InternalFormElement<ModelClass, FieldType, RepositoryType> factoryConfig,
                               FormCreator<ModelClass, FieldType, RepositoryType> formCreator);
}
