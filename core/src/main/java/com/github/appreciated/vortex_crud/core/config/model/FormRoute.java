package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.With;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
@With
public record FormRoute<ModelClass, FieldType, RepositoryType>(
    RepositoryType dataStoreKey,
    String title,
    boolean defaultRoute,
    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean hideInMenu,
    RouteConfig<FieldType> configuration,
    SerializableSupplier<Component> iconFactory,
    List<String> writeRoles,
    List<String> readOnlyRoles,
    Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap
) implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    @SuppressWarnings("unchecked")
    public FormRoute {
        if (factory == null) factory = (Class) FormRouteFactory.class;
        if (childrenMap == null) childrenMap = new HashMap<>();
    }

    public RepositoryType getDataStoreKey() {
        return dataStoreKey;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean isHiddenInMenu() {
        return hideInMenu;
    }

    @Override
    public boolean isDefaultRoute() {
        return defaultRoute;
    }

    public boolean getDefaultRoute() {
        return defaultRoute;
    }

    @Override
    public RouteConfig<FieldType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return factory;
    }

    @Override
    public SerializableSupplier<Component> getIconFactory() {
        return iconFactory;
    }

    public List<String> getWriteRoles() {
        return writeRoles;
    }

    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }

    @Override
    public void setWriteRoles(List<String> writeRoles) {
        throw new UnsupportedOperationException("FormRoute is immutable. Use withWriteRoles() to create a new instance.");
    }

    @Override
    public void setReadOnlyRoles(List<String> readOnlyRoles) {
        throw new UnsupportedOperationException("FormRoute is immutable. Use withReadOnlyRoles() to create a new instance.");
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> getChild() {
        return childrenMap.entrySet().stream().findFirst().map(Map.Entry::getValue).orElse(null);
    }

    public Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> getChildrenMap() {
        return childrenMap;
    }
}