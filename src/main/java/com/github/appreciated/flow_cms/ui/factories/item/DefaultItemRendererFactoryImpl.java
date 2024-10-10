package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultItemRendererFactoryImpl implements FlowCmsItemRendererFactory {

    HashMap<String, FlowCmsItemRenderer> rendererHashMap = new HashMap<>();

    public DefaultItemRendererFactoryImpl() {
        rendererHashMap.put("item-card-renderer", new DefaultItemCardRendererImpl());
    }

    public FlowCmsItemRenderer getRenderer(ItemRendererConfig routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}