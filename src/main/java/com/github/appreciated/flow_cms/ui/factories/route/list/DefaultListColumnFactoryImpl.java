package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.ui.factories.item.DefaultItemCardRendererImpl;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemRenderer;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnFactoryImpl implements FlowCmsListColumnFactory {

    HashMap<String, FlowCmdListColumn> rendererHashMap = new HashMap<>();

    public DefaultListColumnFactoryImpl() {
        rendererHashMap.put("default", new DefaultListColumImpl());
    }

    @Override
    public FlowCmdListColumn getListColumn(RouteConfig config) {
        return rendererHashMap.get("default");
    }
}