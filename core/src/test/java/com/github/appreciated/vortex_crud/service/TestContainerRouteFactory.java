package com.github.appreciated.vortex_crud.service;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class TestContainerRouteFactory implements VortexCrudRouteFactory<String, String> {
    @Override
    public Component renderRoute(Integer currentPathIndex, VortexCrudPathToRouteResolver<String, String> routeResolver, @Nullable DetailRouteSetting detailRouteSetting) {
        return null;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
