package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.ui.factories.router_layout.VortexCrudRouterLayoutFactory;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.RouterLayout;

/**
 * A proxy implementation of the RouterLayout interface.
 * It delegates the layout creation to a factory and proxies the element retrieval to the created RouterLayout.
 */

public class ProxyRouterLayout implements RouterLayout {
    private final RouterLayout routerLayout;

    public ProxyRouterLayout(VortexCrudRouterLayoutFactory factory) {
        routerLayout = factory.createAppLayout();
    }

    @Override
    public Element getElement() {
        return routerLayout.getElement();
    }
}
