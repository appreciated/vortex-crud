package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanFactory;
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
    boolean isDefaultRoute,
    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean isHiddenInMenu,
    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
    SerializableSupplier<Component> iconFactory,
    List<String> writeRoles,
    List<String> readOnlyRoles,
    RouteRenderer<ModelClass, FieldType, RepositoryType> child
) implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    @SuppressWarnings("unchecked")
    public static class KanbanRouteBuilder<ModelClass, FieldType, RepositoryType> {
        KanbanRouteBuilder() {
            factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) KanbanFactory.class;
        }
    }

    public RepositoryType getDataStoreKey() {
        return dataStoreKey;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getWriteRoles() {
        return writeRoles;
    }

    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> child() {
        return child;
    }
}