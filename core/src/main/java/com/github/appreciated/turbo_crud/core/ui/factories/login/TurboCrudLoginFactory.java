package com.github.appreciated.turbo_crud.core.ui.factories.login;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface TurboCrudLoginFactory {
    Component getLoginView(
            Integer currentPathIndex,
            TurboCrudPathToRouteResolver routeResolver
    );
}
