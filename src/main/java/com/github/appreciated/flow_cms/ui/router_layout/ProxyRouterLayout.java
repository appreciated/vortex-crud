package com.github.appreciated.flow_cms.ui.router_layout;

import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.RouterLayout;

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
