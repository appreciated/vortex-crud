package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnCallbackRegistryImpl implements FlowCmsListColumnCallbackRegistry {

    HashMap<String, FlowCmsListColumnCallback> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistryImpl() {
        callbacks.put("default", new DefaultListColumnImplCallback());
    }

    @Override
    public FlowCmsListColumnCallback getCallback(RouteConfig config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, FlowCmsListColumnCallback factory) {
        callbacks.put(key, factory);
    }
}