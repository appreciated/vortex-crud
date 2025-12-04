package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Standard factory implementation for creating {@link VortexCrudListColumnCallback}'s.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> implements VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> {

    private final HashMap<String, VortexCrudListColumnCallback<ModelClass, FieldType, RepositoryType>> callbacks = new HashMap<>();

    public DefaultListColumnCallbackRegistry(
                                             com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldType> reflectionService) {
        callbacks.put("default", new DefaultListColumnImplCallback<ModelClass, FieldType, RepositoryType>(reflectionService));
    }

    @Override
    public VortexCrudListColumnCallback<ModelClass, FieldType, RepositoryType> getCallback(RouteRenderer<ModelClass, FieldType, RepositoryType> config) {
        return callbacks.get("default");
    }

    @Override
    public void addCallback(String key, VortexCrudListColumnCallback<ModelClass, FieldType, RepositoryType> factory) {
        callbacks.put(key, factory);
    }
}
