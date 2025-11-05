package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record MasterDetailRoute<ModelClass, FieldType, RepositoryType>(
    RepositoryType dataStoreKey,
    String title,
    boolean isDefaultRoute,
    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean isHiddenInMenu,
    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
    SerializableSupplier<Component> iconFactory,
    List<String> writeRoles,
    List<String> readOnlyRoles,
    FormRouteProvider<ModelClass, FieldType, RepositoryType> child
) implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>, FormRouteProvider<ModelClass, FieldType, RepositoryType>{

    @Override
    public FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration() {
        return child.formConfiguration();
    }

    @SuppressWarnings("unchecked")
    public static class MasterDetailRouteBuilder<ModelClass, FieldType, RepositoryType> {
        MasterDetailRouteBuilder() {
            factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) MasterDetailRouteFactory.class;
        }
    }
}