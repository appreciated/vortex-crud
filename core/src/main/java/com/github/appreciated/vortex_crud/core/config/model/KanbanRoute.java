package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record KanbanRoute<ModelClass, FieldType, RepositoryType>(
    RepositoryType dataStoreKey,
    String title,
    boolean defaultRoute,
    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean hideInMenu,
    RouteConfig<FieldType> configuration,
    SerializableSupplier<Component> iconFactory,
    List<String> writeRoles,
    List<String> readOnlyRoles,
    RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> child
) implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    @SuppressWarnings("unchecked")
    public KanbanRoute {
        if (factory == null) factory = (Class) KanbanDetailFactory.class;
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

    public RouteRenderer<ModelClass, FieldType, RepositoryType> getChild() {
        return child;
    }
}