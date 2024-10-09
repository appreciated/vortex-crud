package com.github.appreciated.flow_cms.ui.router_layout.components;

import com.github.appreciated.flow_cms.ui.router_layout.FlowCmsRouterLayoutFactory;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.RouterLayout;

/**
 * A proxy implementation of the RouterLayout interface.
 * It delegates the layout creation to a factory and proxies the element retrieval to the created RouterLayout.
 */

public class ProxyRouterLayout implements RouterLayout {
    private final RouterLayout routerLayout;

    public ProxyRouterLayout(FlowCmsRouterLayoutFactory factory) {
        routerLayout = factory.createAppLayout();
    }

    @Override
    public Element getElement() {
        return routerLayout.getElement();
    }
}
