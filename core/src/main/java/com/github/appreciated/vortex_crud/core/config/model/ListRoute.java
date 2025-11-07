package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ListRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>, AccessControlled {

    private RepositoryType dataStoreKey;

    private String title;

    private boolean isDefaultRoute;

    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory;

    private boolean isHiddenInMenu;

    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> columns;

    @SuppressWarnings("unchecked")
    public static class ListRouteBuilder<ModelClass, FieldType, RepositoryType> {
        ListRouteBuilder() {
            factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) ListRouteFactory.class;
        }
    }
}