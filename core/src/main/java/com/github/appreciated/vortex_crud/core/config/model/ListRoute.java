package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record ListRoute<ModelClass, FieldType, RepositoryType>(
        RepositoryType dataStoreKey,
        String title,
        boolean isDefaultRoute,
        Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
        boolean isHiddenInMenu,
        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
        SerializableSupplier<Component> iconFactory,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        RouteRenderer<ModelClass, FieldType, RepositoryType> child,
        List<InternalFormElement<ModelClass, FieldType, RepositoryType>> columns
) implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>, AccessControlled {

    @SuppressWarnings("unchecked")
    public static class ListRouteBuilder<ModelClass, FieldType, RepositoryType> {
        ListRouteBuilder() {
            factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) ListRouteFactory.class;
        }
    }
}