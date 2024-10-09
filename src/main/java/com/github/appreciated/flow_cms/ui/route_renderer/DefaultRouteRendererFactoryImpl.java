package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.entity_detail.FlowCmsEntityDetailRendererFactory;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.FlowCmsEntityItemRendererFactory;
import com.github.appreciated.flow_cms.ui.route_renderer.grid.DefaultGridRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.item_grid.ItemRouteView;
import com.github.appreciated.flow_cms.ui.route_renderer.item_grid.VirtualItemGrid;
import com.github.appreciated.flow_cms.ui.route_renderer.master_detail.DefaultMasterDetailRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.master_detail.MasterDetailRouteView;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the FlowCmsRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteRendererFactoryImpl implements FlowCmsRouteRendererFactory {

    private final DynamicEntityManagerService dynamicEntityManager;
    HashMap<String, RouteRenderer> rendererHashMap = new HashMap<>();

    public DefaultRouteRendererFactoryImpl(DynamicEntityManagerService dynamicEntityManager, FlowCmsEntityItemRendererFactory entityCardRendererFactory, FlowCmsEntityDetailRendererFactory detailRendererFactory) {
        this.dynamicEntityManager = dynamicEntityManager;
        rendererHashMap.put("master-detail", (i, config, entityManagerService) -> new MasterDetailRouteView(i, config, dynamicEntityManager, entityCardRendererFactory, detailRendererFactory));
        rendererHashMap.put("grid", (i, config, entityManagerService) -> new DefaultGridRenderer(i, config, dynamicEntityManager));
        rendererHashMap.put("item", (i, config, entityManagerService) -> new ItemRouteView(i, config, dynamicEntityManager, entityCardRendererFactory));
    }

    public Component createViewContainer(RouteConfig routeConfig) {
        return rendererHashMap.get(routeConfig.getRenderer()).renderRoute(0, routeConfig, dynamicEntityManager);
    }
}
