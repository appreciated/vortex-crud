package com.github.appreciated.vortex_crud.core.ui.factories.route.search;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.routes.search.SearchRouteView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class SearchRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer = routeResolver.getCurrentRoute();
        return new SearchRouteView<>(context, routeRenderer);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
