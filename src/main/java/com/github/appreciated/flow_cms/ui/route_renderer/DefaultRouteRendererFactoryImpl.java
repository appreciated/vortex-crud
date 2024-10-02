package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.route_renderer.cards.CardRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.grid.GridRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.master_detail.MasterDetailRenderer;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultRouteRendererFactoryImpl implements FlowCmsRouteRendererFactory {

    private final DynamicEntityManagerService dynamicEntityManager;

    public DefaultRouteRendererFactoryImpl(DynamicEntityManagerService dynamicEntityManager) {
        this.dynamicEntityManager = dynamicEntityManager;
    }

    public Component createViewContainer(ConfigObject config) {
        String renderer = config.toConfig().getString("renderer");
        return switch (renderer) {
            case "master_detail" -> new MasterDetailRenderer(0, config, dynamicEntityManager);
            case "grid" -> new GridRenderer(0, config, dynamicEntityManager);
            case "card" -> new CardRenderer(0, config, dynamicEntityManager);
            default -> throw new IllegalStateException();
        };
    }
}
