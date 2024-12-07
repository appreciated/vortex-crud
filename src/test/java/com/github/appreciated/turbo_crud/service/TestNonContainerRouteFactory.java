package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;
import org.jetbrains.annotations.Nullable;

public class TestNonContainerRouteFactory implements TurboCrudRouteFactory {
    @Override
    public Component renderRoute(Integer currentPathIndex, TurboCrudPathToRouteResolver routeResolver, @Nullable DetailRouteSetting detailRouteSetting) {
        return null;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
