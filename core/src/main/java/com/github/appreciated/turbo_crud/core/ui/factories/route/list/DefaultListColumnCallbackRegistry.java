package com.github.appreciated.turbo_crud.core.ui.factories.route.list;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Standard factory implementation for creating {@link TurboCrudListColumnCallback}'s.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnCallbackRegistry<DataStoreId, FieldId> implements TurboCrudListColumnCallbackRegistry<DataStoreId, FieldId> {

    private final HashMap<String, TurboCrudListColumnCallback<DataStoreId, FieldId>> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistry(TurboCrudFileProviderRegistry fileProviderRegistry) {
        callbacks.put("default", new DefaultListColumnImplCallback<>(fileProviderRegistry));
    }

    @Override
    public TurboCrudListColumnCallback<DataStoreId, FieldId> getCallback(Route<DataStoreId, FieldId> config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, TurboCrudListColumnCallback<DataStoreId, FieldId> factory) {
        callbacks.put(key, factory);
    }
}