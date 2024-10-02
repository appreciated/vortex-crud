package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.route_renderer.grid.GridRenderer;
import com.github.appreciated.flow_cms.ui.route_renderer.master_detail.MasterDetailRenderer;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultRouteRendererFactoryImpl implements FlowCmsRouteRendererFactory {

    private final DynamicEntityManagerService dynamicEntityManagerService;

    public DefaultRouteRendererFactoryImpl(DynamicEntityManagerService dynamicEntityManagerService) {
        this.dynamicEntityManagerService = dynamicEntityManagerService;
    }

    public Component createViewContainer(ConfigObject config) {
        String renderer = config.toConfig().getString("renderer");
        switch (renderer){
            case "master_detail" -> {
                return new MasterDetailRenderer(0, config, dynamicEntityManagerService);
            }
            case "grid" -> {
                return new GridRenderer(0, config, dynamicEntityManagerService);
            }
            default -> throw new IllegalStateException();
        }
    }
}
