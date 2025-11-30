package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link VortexCrudDialogFactoryRegistry} interface.
 * This factory provides components based on the configuration specified in {@link com.github.appreciated.vortex_crud.core.config.model.Field}.
 */

@Service
public class DefaultDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> implements VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    private final Map<Class<?>, VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> factories = new HashMap<>();

    public DefaultDialogFactoryRegistry() {
        factories.put(FormDialogFactory.class, new FormDialogFactory<>());
        factories.put(FormRouteFactory.class, new FormDialogFactory<>());
        factories.put(MultiFormRouteFactory.class, new FormDialogFactory<>());
        factories.put(FormSlideRouteFactory.class, new FormSlideFactory<>());
        factories.put(ConnectDialogFactory.class, new ConnectDialogFactory<>());
    }

    public Map<Class<?>, VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<?> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<?> key, VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> factory) {
        factories.put(key, factory);
    }
}
