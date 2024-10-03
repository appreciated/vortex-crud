package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.route_renderer.cards.VirtualCardGridRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.grid.GridRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.master_detail.MasterDetailRenderer;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DefaultRouteRendererFactoryImpl implements FlowCmsRouteRendererFactory {

    HashMap<String, RouteRenderer> rendererHashMap = new HashMap<>();

    private final DynamicEntityManagerService dynamicEntityManager;

    public DefaultRouteRendererFactoryImpl(DynamicEntityManagerService dynamicEntityManager) {
        this.dynamicEntityManager = dynamicEntityManager;
        rendererHashMap.put("master_detail", (i, config, entityManagerService) -> new MasterDetailRenderer(i, config, dynamicEntityManager));
        rendererHashMap.put("grid", (i, config, entityManagerService) -> new GridRenderer(i, config, dynamicEntityManager));
        rendererHashMap.put("card", (i, config, entityManagerService) -> new VirtualCardGridRenderer(i, config, dynamicEntityManager));
    }

    public Component createViewContainer(ConfigObject config) {
        String renderer = config.toConfig().getString("renderer");
        return rendererHashMap.get(renderer).renderRoute(0, config, dynamicEntityManager);
    }
}
