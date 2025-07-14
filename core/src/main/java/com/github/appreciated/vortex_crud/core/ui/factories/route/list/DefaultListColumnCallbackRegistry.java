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
public class DefaultListColumnCallbackRegistry<DataStoreId, FieldId> implements VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId> {

    private final HashMap<String, VortexCrudListColumnCallback<DataStoreId, FieldId>> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistry(VortexCrudFileProviderRegistry fileProviderRegistry, 
                                         com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldId> reflectionService) {
        callbacks.put("default", new DefaultListColumnImplCallback<DataStoreId, FieldId>(fileProviderRegistry, reflectionService));
    }

    @Override
    public VortexCrudListColumnCallback<DataStoreId, FieldId> getCallback(RouteRenderer<DataStoreId, FieldId> config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, VortexCrudListColumnCallback<DataStoreId, FieldId> factory) {
        callbacks.put(key, factory);
    }
}
