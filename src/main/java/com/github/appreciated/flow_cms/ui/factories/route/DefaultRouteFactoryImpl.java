package com.github.appreciated.flow_cms.ui.factories.route;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsEntityDetailFactory;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemRendererFactory;
import com.github.appreciated.flow_cms.ui.factories.route.list.DefaultListRouteImpl;
import com.github.appreciated.flow_cms.ui.factories.route.grid.DefaultGridRouteImpl;
import com.github.appreciated.flow_cms.ui.factories.route.master_detail.DefaultMasterDetailRouteImpl;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the FlowCmsRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryImpl implements FlowCmsRouteFactory {

    private final DynamicEntityManagerService dynamicEntityManager;
    HashMap<String, FlowCmdRoute> rendererHashMap = new HashMap<>();

    public DefaultRouteFactoryImpl(DynamicEntityManagerService dynamicEntityManager, FlowCmsItemRendererFactory entityCardRendererFactory, FlowCmsEntityDetailFactory detailRendererFactory) {
        this.dynamicEntityManager = dynamicEntityManager;
        rendererHashMap.put("master-detail", (i, config, entityManagerService) -> new DefaultMasterDetailRouteImpl(i, config, dynamicEntityManager, entityCardRendererFactory, detailRendererFactory));
        rendererHashMap.put("list", (i, config, entityManagerService) -> new DefaultListRouteImpl(i, config, dynamicEntityManager));
        rendererHashMap.put("grid", (i, config, entityManagerService) -> new DefaultGridRouteImpl(i, config, dynamicEntityManager, entityCardRendererFactory));
    }

    public Component createViewContainer(RouteConfig routeConfig) {
        return rendererHashMap.get(routeConfig.getRenderer()).renderRoute(0, routeConfig, dynamicEntityManager);
    }
}
