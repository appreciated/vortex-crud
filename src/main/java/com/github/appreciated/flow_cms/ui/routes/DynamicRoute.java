package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.components.FlowCmsComponentFactory;
import com.github.appreciated.flow_cms.ui.view_container.DefaultViewContainerContainerFactory;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.stereotype.Service;


/**
 * The {@link DynamicRouteGenerator} forwards all requests matching a view route to this class.
 * Depending on the route a different component should be rendered using the {@link FlowCmsComponentFactory}.
 */
@Service
public class DynamicRoute extends Div {

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
        String url = RouteConfiguration.forSessionScope()
                .getUrl(DynamicRoute.class);

        removeAll(); // Clear existing components
        ConfigObject configForRoute = getConfigForRoute(url);
        add(createComponentFromConfig(configForRoute));

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

    /**
     * Creates a component based on the provided configuration.
     *
     * @param viewConfig the configuration for the view
     * @return the component created from the configuration
     */
    private Component createComponentFromConfig(ConfigObject viewConfig) {
        if (viewConfig != null) {
            return defaultRouteFactory.createViewContainer(viewConfig);
        }
        return new VerticalLayout();
    }
}