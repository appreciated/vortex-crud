package com.github.appreciated.vortex_crud.service;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class TestNonContainerRouteFactory implements VortexCrudRouteFactory<String, String, String> {
    @Override
    public Component renderRoute(VortexCrudContext<String, String, String> context,
                                 Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<String, String, String> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return null;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
