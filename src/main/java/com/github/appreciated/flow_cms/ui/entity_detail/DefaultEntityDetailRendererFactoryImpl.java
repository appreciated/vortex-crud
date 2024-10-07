package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.components.DefaultFlowCmsComponentFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DefaultEntityDetailRendererFactoryImpl implements FlowCmsEntityDetailRendererFactory {

    HashMap<String, EntityDetailRenderer> rendererHashMap = new HashMap<>();

    public DefaultEntityDetailRendererFactoryImpl(DefaultFlowCmsComponentFactoryImpl componentFactory, DynamicEntityManagerService entityManagerService, FlowCmsConfigService flowCmsConfigService) {
        rendererHashMap.put("entity_item_card_renderer", new DefaultEntityDetailRendererImpl(componentFactory, entityManagerService, flowCmsConfigService));
    }

    public EntityDetailRenderer getRenderer(DetailRenderer routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}