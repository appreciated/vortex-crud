package com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class MasterDetailRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting) {
        return new MasterDetail<>(currentPathIndex,
                routeResolver,
                context
        );
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
