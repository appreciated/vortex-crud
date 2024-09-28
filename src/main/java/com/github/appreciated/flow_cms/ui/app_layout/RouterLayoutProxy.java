package com.github.appreciated.flow_cms.ui.app_layout;

import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.RouterLayout;

public class RouterLayoutProxy implements RouterLayout {
    private final RouterLayout routerLayout;

    public RouterLayoutProxy(FlowCmsRouterLayoutFactory factory) {
        routerLayout = factory.createAppLayout();
    }

    @Override
    public Element getElement() {
        return routerLayout.getElement();
    }
}
