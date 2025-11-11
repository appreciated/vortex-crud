package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.custom.CustomRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * A route that allows complete customization of the rendered component through a custom renderer.
 * This route provides maximum flexibility for creating custom views that don't fit the standard patterns.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CustomRoute.builder()
 *     .dataStoreKey(myRepository)
 *     .title("custom.view")
 *     .componentRenderer(context -> {
 *         VerticalLayout layout = new VerticalLayout();
 *         // Build custom component using context.getDataStore(), etc.
 *         return layout;
 *     })
 *     .build()
 * }</pre>
 *
 * @param <ModelClass>     The entity model class
 * @param <FieldType>      The field type (typically an enum or descriptor)
 * @param <RepositoryType> The repository/data store key type
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CustomRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> {

    private RepositoryType dataStoreKey;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) CustomRouteFactory.class;

    private boolean isHiddenInMenu;

    /**
     * The custom component renderer that will be invoked to render this route.
     * This renderer receives a context with access to all necessary services and configuration.
     */
    private CustomComponentRenderer<ModelClass, FieldType, RepositoryType> componentRenderer;

    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    /**
     * Optional child route. If provided, the child can be rendered within the custom component
     * by accessing it through the route configuration.
     */
    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    /**
     * List of menu actions for adding custom components to the menu.
     * This can include dropdowns, filters, action buttons, etc.
     */
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    @Override
    public RepositoryType dataStoreKey() {
        return dataStoreKey;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public List<String> writeRoles() {
        return writeRoles;
    }

    @Override
    public List<String> readOnlyRoles() {
        return readOnlyRoles;
    }
}
