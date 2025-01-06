package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.vaadin.flow.router.RouterLayout;

/**
 * Interface for factories that create RouterLayout instances.
 * Implementations should provide a method for generating application layouts compatible with Vaadin's RouterLayout.
 */

public interface VortexCrudRouterLayoutFactory {

    RouterLayout createAppLayout();

}