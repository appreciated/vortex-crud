package com.github.appreciated.flow_cms.ui.entity_item_renderer.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultEntityItemRendererFactoryImpl implements FlowCmsEntityItemRendererFactory {

    HashMap<String, EntityItemRenderer> rendererHashMap = new HashMap<>();

    public DefaultEntityItemRendererFactoryImpl() {
        rendererHashMap.put("entity_item_card_renderer", new DefaultEntityItemCardRendererImpl());
    }

    public EntityItemRenderer getRenderer(ItemRendererConfig routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}