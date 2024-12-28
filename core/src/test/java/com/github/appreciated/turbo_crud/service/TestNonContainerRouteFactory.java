package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class TestNonContainerRouteFactory implements TurboCrudRouteFactory<String> {
    @Override
    public Component renderRoute(Integer currentPathIndex, TurboCrudPathToRouteResolver<String> routeResolver, @Nullable DetailRouteSetting detailRouteSetting) {
        return null;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
