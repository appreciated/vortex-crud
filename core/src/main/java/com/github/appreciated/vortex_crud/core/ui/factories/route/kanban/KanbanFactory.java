package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component.KanbanView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class KanbanFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
             VortexCrudPathToRouteResolver routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting) {

        RouteRenderer<?, ?, ?> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<>(
                routeRenderer.dataStoreConfig().factory(),
                routeRenderer,
                context,
                routeResolver,
                detailRouteSetting
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
