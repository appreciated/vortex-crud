package com.github.appreciated.vortex_crud.core.ui.factories.route;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    );

    /**
     * Whether the route implementation is able / required to render route children
     * F.e. The master-detail route is a container route since on the right hand site another route is being rendered
     */
    boolean isContainerRoute();
}
