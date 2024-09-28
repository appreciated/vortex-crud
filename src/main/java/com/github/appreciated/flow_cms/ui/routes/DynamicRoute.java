package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.app_layout.RouterLayoutProxy;
import com.github.appreciated.flow_cms.ui.components.FlowCmsComponentFactory;
import com.github.appreciated.flow_cms.ui.view_container.DefaultViewContainerContainerFactory;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;


/**
 * The {@link DynamicRouteGenerator} forwards all requests matching a view route to this class.
 * Depending on the route a different component should be rendered using the {@link FlowCmsComponentFactory}.
 */
@Component
@Route(layout = RouterLayoutProxy.class)
public class DynamicRoute extends Div {

    public static final String VIEW_PATH_PREFIX = "view/";
    private final FlowCmsConfigService flowCmsConfigService;
    private final DefaultViewContainerContainerFactory defaultRouteFactory;

    /**
     * Constructor for DynamicView.
     *
     * @param flowCmsConfigService    the service to retrieve configuration for the routes
     */
    public DynamicRoute(FlowCmsConfigService flowCmsConfigService, DefaultViewContainerContainerFactory defaultRouteFactory) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.defaultRouteFactory = defaultRouteFactory;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Get the current route location from UI
        String currentRoute = UI.getCurrent().getInternals().getActiveViewLocation().getPath();
        if (currentRoute.startsWith(VIEW_PATH_PREFIX)) {
            currentRoute = currentRoute.substring(VIEW_PATH_PREFIX.length());
        }
        removeAll(); // Clear existing components
        ConfigObject configForRoute = getConfigForRoute(currentRoute);
        add(defaultRouteFactory.createViewContainer(configForRoute));
        super.onAttach(attachEvent);
    }

    /**
     * Retrieves the configuration for the given route.
     *
     * @param route the route for which the configuration is needed
     * @return the configuration for the route
     */
    private ConfigObject getConfigForRoute(String route) {
        return flowCmsConfigService.getForRoute(route);
    }
}