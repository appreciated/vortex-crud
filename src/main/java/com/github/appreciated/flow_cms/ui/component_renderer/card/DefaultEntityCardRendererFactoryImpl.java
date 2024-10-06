package com.github.appreciated.flow_cms.ui.component_renderer.card;

import com.github.appreciated.flow_cms.config.model.CardRendererConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DefaultEntityCardRendererFactoryImpl implements FlowCmsEntityCardRendererFactory {

    HashMap<String, CardRenderer> rendererHashMap = new HashMap<>();

    public DefaultEntityCardRendererFactoryImpl() {
        rendererHashMap.put("entity_card_renderer", new CardRendererImpl());
    }

    public CardRenderer getCardRenderer(CardRendererConfig routeConfig) {
        return rendererHashMap.get(routeConfig.getType());
    }
}