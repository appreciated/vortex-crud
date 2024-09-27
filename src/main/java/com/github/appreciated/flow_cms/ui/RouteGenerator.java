package com.github.appreciated.flow_cms.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import java.util.HashMap;
import java.util.Map;

public class RouteGenerator {

    private DynamicViewFactory viewFactory;
    private Map<String, Class<? extends Component>> routeMap = new HashMap<>();

    public RouteGenerator(DynamicViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    public void registerRoute(String viewName) {
        Component view = viewFactory.createView(viewName);
        routeMap.put(viewName, view.getClass());
        // Dynamische Route mit Vaadin API registrieren
        Route routeAnnotation = new Route(viewName);
        // Route zu Vaadin's Routing-System hinzufügen (z.B. mit Reflection)
    }
}
