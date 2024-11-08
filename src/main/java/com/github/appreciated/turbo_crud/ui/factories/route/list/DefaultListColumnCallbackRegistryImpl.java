package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Standard factory implementation for creating {@link TurboCrudListColumnCallback}'s.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnCallbackRegistryImpl implements TurboCrudListColumnCallbackRegistry {

    private final HashMap<String, TurboCrudListColumnCallback> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistryImpl(TurboCrudFileProviderRegistry fileProviderRegistry) {
        callbacks.put("default", new DefaultListColumnImplCallback(fileProviderRegistry));
    }

    @Override
    public TurboCrudListColumnCallback getCallback(Route config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, TurboCrudListColumnCallback factory) {
        callbacks.put(key, factory);
    }
}