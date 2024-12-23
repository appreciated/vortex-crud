package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class TestContainerRouteFactory implements TurboCrudRouteFactory {
    @Override
    public Component renderRoute(Integer currentPathIndex, TurboCrudPathToRouteResolver routeResolver, @Nullable DetailRouteSetting detailRouteSetting) {
        return null;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
