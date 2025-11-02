package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.With;

import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
@With
public record SubmenuRoute<ModelClass, FieldType, RepositoryType>(
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
) implements RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType> {

    @SuppressWarnings("unchecked")
    public SubmenuRoute {
        if (factory == null) factory = (Class) SubmenuRouteFactory.class;
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

    public Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> getChildrenMap() {
        return childrenMap;
    }
}