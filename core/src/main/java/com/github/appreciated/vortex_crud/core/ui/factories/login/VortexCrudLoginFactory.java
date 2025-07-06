package com.github.appreciated.vortex_crud.core.ui.factories.login;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface VortexCrudLoginFactory<DataStoreId, FieldId, ModelClass> {
    Component getLoginView(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> routeResolver
    );
}
