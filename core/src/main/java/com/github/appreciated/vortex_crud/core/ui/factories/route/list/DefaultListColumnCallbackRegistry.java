package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Standard factory implementation for creating {@link VortexCrudListColumnCallback}'s.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnCallbackRegistry<DataStoreId, FieldId, ModelClass> implements VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, ModelClass> {

    private final HashMap<String, VortexCrudListColumnCallback<DataStoreId, FieldId, ModelClass>> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistry(VortexCrudFileProviderRegistry fileProviderRegistry) {
        callbacks.put("default", new DefaultListColumnImplCallback<>(fileProviderRegistry));
    }

    @Override
    public VortexCrudListColumnCallback<DataStoreId, FieldId, ModelClass> getCallback(RouteRenderer<DataStoreId, FieldId, ModelClass> config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, VortexCrudListColumnCallback<DataStoreId, FieldId, ModelClass> factory) {
        callbacks.put(key, factory);
    }
}