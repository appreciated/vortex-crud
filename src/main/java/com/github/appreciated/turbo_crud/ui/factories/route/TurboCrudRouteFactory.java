package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.config.TurboCrudPathSegments;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface TurboCrudRouteFactory {
    Component renderRoute(
            TurboCrudPathSegments pathVariables,
            String table,
            String title,
            Route route,
            boolean isWrapped,
            boolean hideHeader
    );
}
