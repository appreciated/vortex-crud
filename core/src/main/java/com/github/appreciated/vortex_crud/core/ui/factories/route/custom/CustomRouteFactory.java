package com.github.appreciated.vortex_crud.core.ui.factories.route.custom;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nullable;

/**
 * No-op factory for CustomRoute. Should never be called since @Route handles the view.
 */
public class CustomRouteFactory<ModelClass, FieldType, RepositoryType>
        implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        Div error = new Div();
        error.setText("CustomRoute misconfigured - check @Route annotation and path match");
        error.getStyle().set("color", "red").set("padding", "2em");
        return error;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
