package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.RouteConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Standard factory implementation for creating {@link TurboCrudListColumnCallback}'s.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnCallbackRegistryImpl implements TurboCrudListColumnCallbackRegistry {

    HashMap<String, TurboCrudListColumnCallback> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistryImpl() {
        callbacks.put("default", new DefaultListColumnImplCallback());
    }

    @Override
    public TurboCrudListColumnCallback getCallback(RouteConfig config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, TurboCrudListColumnCallback factory) {
        callbacks.put(key, factory);
    }
}