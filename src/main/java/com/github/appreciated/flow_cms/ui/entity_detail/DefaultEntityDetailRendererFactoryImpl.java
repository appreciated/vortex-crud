package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.fields.DefaultFlowCmsFieldFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity detail renderers.
 * It initializes and provides the appropriate renderer based on the DetailRenderer configuration.
 */

@Service
public class DefaultEntityDetailRendererFactoryImpl implements FlowCmsEntityDetailRendererFactory {

    HashMap<String, FlowCmsEntityDetailRenderer> rendererHashMap = new HashMap<>();

    public DefaultEntityDetailRendererFactoryImpl(DefaultFlowCmsFieldFactoryImpl componentFactory, DynamicEntityManagerService entityManagerService, FlowCmsConfigService flowCmsConfigService) {
        rendererHashMap.put("form", new DefaultFormEntityDetailRendererImpl(componentFactory, entityManagerService, flowCmsConfigService));
    }

    public FlowCmsEntityDetailRenderer getRenderer(DetailRenderer routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}


