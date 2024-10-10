package com.github.appreciated.flow_cms.ui.factories.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultEntityItemRendererFactoryImpl implements FlowCmsEntityItemRendererFactory {

    HashMap<String, FlowCmsEntityItemRenderer> rendererHashMap = new HashMap<>();

    public DefaultEntityItemRendererFactoryImpl() {
        rendererHashMap.put("entity-item-card-renderer", new DefaultEntityItemCardRendererImpl());
    }

    public FlowCmsEntityItemRenderer getRenderer(ItemRendererConfig routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}