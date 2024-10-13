package com.github.appreciated.turbo_crud.ui.factories.router_layout;

import com.vaadin.flow.router.RouterLayout;

/**
 * Interface for factories that create RouterLayout instances.
 * Implementations should provide a method for generating application layouts compatible with Vaadin's RouterLayout.
 */

public interface TurboCrudRouterLayoutFactory {

    RouterLayout createAppLayout();

}