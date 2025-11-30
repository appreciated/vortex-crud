package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.KanbanConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component.KanbanView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class KanbanFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    @SuppressWarnings("unchecked")
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting,
                                 VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<>(routeRenderer.dataStoreKey(),
                routeRenderer,
                (KanbanConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration(),
                detailRouteSetting,
                routeResolver,
                context
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
