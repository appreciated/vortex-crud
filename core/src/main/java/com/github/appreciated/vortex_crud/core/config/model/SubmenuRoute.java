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
    boolean isDefaultRoute,
    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean isHiddenInMenu,
    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
    SerializableSupplier<Component> iconFactory,
    List<String> writeRoles,
    List<String> readOnlyRoles,
    Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap
) implements RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType> {

    @SuppressWarnings("unchecked")
    public static class SubmenuRouteBuilder<ModelClass, FieldType, RepositoryType> {
        SubmenuRouteBuilder() {
            factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) SubmenuRouteFactory.class;
        }
    }
}