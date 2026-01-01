package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;

public interface VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> {
    Component createCollection(Object foreignKeyValue,
                               RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                               Collection<ModelClass, FieldType, RepositoryType> factoryConfig,
                               VortexCrudContext<ModelClass, FieldType, RepositoryType> context);
}
