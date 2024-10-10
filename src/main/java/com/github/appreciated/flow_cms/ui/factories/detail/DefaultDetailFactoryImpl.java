package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.fields.DefaultFlowCmsFieldFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity detail renderers.
 * It initializes and provides the appropriate renderer based on the DetailRenderer configuration.
 */

@Service
public class DefaultDetailFactoryImpl implements FlowCmsEntityDetailFactory {

    HashMap<String, FlowCmsDetail> rendererHashMap = new HashMap<>();

    public DefaultDetailFactoryImpl(DefaultFlowCmsFieldFactoryImpl componentFactory, DynamicEntityManagerService entityManagerService, FlowCmsConfigService flowCmsConfigService) {
        rendererHashMap.put("form", new DefaultFormDetailImpl(componentFactory, entityManagerService, flowCmsConfigService));
    }

    public FlowCmsDetail getRenderer(DetailRenderer routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}


