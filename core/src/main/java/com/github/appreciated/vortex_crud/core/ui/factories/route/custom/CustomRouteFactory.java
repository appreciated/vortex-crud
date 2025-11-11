package com.github.appreciated.vortex_crud.core.ui.factories.route.custom;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.CustomComponentRenderer;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

/**
 * Factory implementation for rendering CustomRoute instances.
 * This factory delegates to the custom component renderer provided in the route configuration.
 *
 * @param <ModelClass>     The entity model class
 * @param <FieldType>      The field type (typically an enum or descriptor)
 * @param <RepositoryType> The repository/data store key type
 */
public class CustomRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;

    public CustomRouteFactory(
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
    }

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        // Verify the route is actually a CustomRoute
        if (!(routeResolver.getRouteForIndex(currentPathIndex) instanceof CustomRoute)) {
            throw new IllegalStateException(
                    "CustomRouteFactory can only render CustomRoute instances, but got: " +
                            routeResolver.getRouteForIndex(currentPathIndex).getClass().getName()
            );
        }

        CustomRoute<ModelClass, FieldType, RepositoryType> customRoute =
                (CustomRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);

        // Verify a component renderer is provided
        if (customRoute.componentRenderer() == null) {
            throw new IllegalStateException(
                    "CustomRoute must have a componentRenderer configured. " +
                            "Use .componentRenderer(context -> { ... }) in the builder."
            );
        }

        // Build the render context
        CustomComponentRenderer.RenderContext<ModelClass, FieldType, RepositoryType> context =
                new CustomComponentRenderer.RenderContext<>(
                        currentPathIndex,
                        routeResolver,
                        detailRouteSetting,
                        dataStoreFactoryRegistry,
                        configService,
                        customRoute
                );

        // Delegate to the custom renderer
        return customRoute.componentRenderer().render(context);
    }

    @Override
    public boolean isContainerRoute() {
        // CustomRoute is not a container route by default
        // Users can implement container-like behavior in their custom renderer if needed
        return false;
    }
}
